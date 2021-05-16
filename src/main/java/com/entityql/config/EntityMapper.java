package com.entityql.config;

import com.querydsl.core.QueryException;
import com.querydsl.core.types.Path;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.Mapper;
import com.querydsl.sql.types.Null;
import org.hibernate.annotations.common.reflection.ReflectionUtil;
import org.hibernate.mapping.Join;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityMapper implements Mapper<Object> {

    public static final EntityMapper DEFAULT = new EntityMapper(false);

    public static final EntityMapper WITH_NULL_BINDINGS = new EntityMapper(true);

    private final boolean withNullBindings;

    public EntityMapper(boolean withNullBindings) {
        this.withNullBindings = withNullBindings;
    }

    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> path, Object object) {
        try {
            Map<String, Path<?>> columnToPath = new HashMap<>();
            for(Path<?> column : path.getColumns()) {
                columnToPath.put(ColumnMetadata.getName(column), column);
            }

            Map<Path<?>, Object> values = new HashMap<>();
            for(Field field : ReflectionUtils.getFields(object.getClass())) {
                putByEmbedded(object, columnToPath, values, field);
                putByColumn(object, columnToPath, values, field);
                putByJoinColumn(object, columnToPath, values, field);
            }

            return values;
        } catch (IllegalAccessError | IllegalAccessException e) {
            throw new QueryException(e);
        }
    }

    private void putByEmbedded(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        Embedded embedded = field.getAnnotation(Embedded.class);
        if(embedded != null) {
            field.setAccessible(true);
            Object o = field.get(object);
            if(o != null) {
                for(Field embeddedField : ReflectionUtils.getFields(o.getClass())) {
                    putByColumn(o, columnToPath, values, embeddedField);
                }
            }
        }
    }

    private void putByColumn(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        Column column = field.getAnnotation(Column.class);
        if(column != null) {
            field.setAccessible(true);
            Object value = field.get(object);
            String columName = column.name();
            if(value != null) {
                if(columnToPath.containsKey(columName)) {
                    values.put(columnToPath.get(columName), value);
                }
            }
            else if(withNullBindings) {
                values.put(columnToPath.get(columName), Null.DEFAULT);
            }
        }
    }

    private void putByJoinColumn(Object object, Map<String, Path<?>> columnToPath, Map<Path<?>, Object> values, Field field) throws IllegalAccessException {
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        if(joinColumn != null) {
            field.setAccessible(true);
            Object joinObject = field.get(object);
            String columnName = joinColumn.name();
            String joinColumnName = joinColumn.referencedColumnName();

            if(joinObject != null) {
                if(columnToPath.containsKey(columnName)) {
                    Object joinColumnValue = getJoinColumnValue(joinObject, joinColumnName);
                    if(joinColumnValue != null) {
                        values.put(columnToPath.get(columnName), joinColumnValue);
                    }
                }
            } else if(withNullBindings) {
                values.put(columnToPath.get(columnName), Null.DEFAULT);
            }
        }
    }

    private Object getJoinColumnValue(Object joinObject, String joinColumnName) throws IllegalAccessException {
        for(Field field : ReflectionUtils.getFields(joinObject.getClass())) {
            Column column = field.getAnnotation(Column.class);
            if(column != null &&column.name().equals(joinColumnName)) {
                field.setAccessible(true);
                return field.get(joinObject);
            }
        }
        return null;
    }
}
