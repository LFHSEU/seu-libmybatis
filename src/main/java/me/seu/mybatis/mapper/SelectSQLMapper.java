package me.seu.mybatis.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by liangfeihu on 2017/3/11.
 */
public interface SelectSQLMapper<T> {
    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<T> listQueryBySQL(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<T> listPageBySQL(Map<String, Object> params, RowBounds rowBounds);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    T singleBySQL(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    int countBySQL(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<T> nativeListQuery(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<String> nativeListQueryReturnString(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<Long> nativeListQueryReturnLong(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    Integer nativeSingleQueryReturnInteger(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    Object nativeSingleQueryReturnObject(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<Object> nativeObjectListQuery(Map<String, Object> params);

    @SelectProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    List<Map<String, Object>> nativeMapListQuery(Map<String, Object> params);

    @UpdateProvider(type = SelectSQLProvider.class, method = "dynamicSQL")
    int nativeUpdate(Map<String, Object> params);
}
