package me.seu.mybatis.mapper;

import me.seu.mybatis.ShardKey;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.*;

import javax.persistence.Version;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateSQLProvider extends MapperTemplate {
    public UpdateSQLProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    private static final Map<Class<?>, String> hasShardKeyEntity = new ConcurrentHashMap<>();
    private static final Set<Class<?>> notHasShardKeyEntity = ConcurrentHashMap.newKeySet();
    private static final Map<Class<?>, String> hasVersionEntity = new ConcurrentHashMap<>();
    private static final Set<Class<?>> notHasVersionEntity = ConcurrentHashMap.newKeySet();

    private String getVersion(Class<?> entityClass) {
        if (hasVersionEntity.containsKey(entityClass)) {
            return hasVersionEntity.get(entityClass);
        }
        if (notHasVersionEntity.contains(entityClass)) {
            return null;
        }
        List<EntityField> fields = FieldHelper.getFields(entityClass);
        for (EntityField field : fields) {
            if (field.isAnnotationPresent(Version.class)) {
                hasVersionEntity.put(entityClass, field.getName());
                return field.getName();
            }
        }
        notHasVersionEntity.add(entityClass);
        return null;
    }

    private String getShardKey(Class<?> entityClass) {
        if (hasShardKeyEntity.containsKey(entityClass)) {
            return hasShardKeyEntity.get(entityClass);
        }
        if (notHasShardKeyEntity.contains(entityClass)) {
            return null;
        }
        List<EntityField> fields = FieldHelper.getFields(entityClass);
        for (EntityField field : fields) {
            if (field.isAnnotationPresent(ShardKey.class)) {
                hasShardKeyEntity.put(entityClass, field.getName());
                return field.getName();
            }
        }
        notHasShardKeyEntity.add(entityClass);
        return null;
    }


    public String update(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        String entityName = null;
        boolean notNull = false;
        boolean notEmpty = false;

        String shardKey = getShardKey(entityClass);
        EntityColumn shardKeyColumn = null;
        String version = getVersion(entityClass);
        EntityColumn versionColumn = null;
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));

        sql.append("<set>");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        Iterator iterator = columnList.iterator();
        while (iterator.hasNext()) {
            EntityColumn column = (EntityColumn) iterator.next();
            if (!column.isId() && column.isUpdatable()) {
                if (shardKey != null && shardKey.equals(column.getColumn())) {
                    shardKeyColumn = column;
                    continue;
                }
                if (version != null && version.equals(column.getColumn())) {
                    versionColumn = column;
                }
                sql.append(column.getColumnEqualsHolder(entityName) + ",");
            }
        }
        sql.append("</set>");

        sql.append("<where>");
        if (shardKeyColumn != null) {
            sql.append(" AND " + shardKeyColumn.getColumnEqualsHolder(entityName));
        }

        Set<EntityColumn> columnPKList = EntityHelper.getPKColumns(entityClass);
        Iterator pkIterator = columnPKList.iterator();
        while (pkIterator.hasNext()) {
            EntityColumn column = (EntityColumn) pkIterator.next();
            sql.append(" AND " + column.getColumnEqualsHolder());
        }

        if (versionColumn != null) {
            sql.append(" AND " + versionColumn.getColumnEqualsHolder(entityName) + " -1");
        }
        sql.append("</where>");
        return sql.toString();
    }


}
