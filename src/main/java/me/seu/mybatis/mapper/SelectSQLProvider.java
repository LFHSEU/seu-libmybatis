package me.seu.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * Created by liangfeihu on 2017/3/11.
 */
public class SelectSQLProvider extends MapperTemplate {
    public SelectSQLProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String listQueryBySQL(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(" ${sql}");
        return sql.toString();
    }

    public String listPageBySQL(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(" ${sql}");
        return sql.toString();
    }

    public String singleBySQL(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(" ${sql}");
        return sql.toString();
    }


    public String countBySQL(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1)");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(" ${sql}");
        return sql.toString();
    }

    public String nativeListQuery(MappedStatement ms) {
        Class entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        return "${sql}";
    }

    public String nativeListQueryReturnString(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeListQueryReturnLong(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeSingleQueryReturnInteger(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeSingleQueryReturnObject(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeObjectListQuery(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeMapListQuery(MappedStatement ms) {
        return "${sql}";
    }

    public String nativeUpdate(MappedStatement ms) {
        return "${sql}";
    }
}
