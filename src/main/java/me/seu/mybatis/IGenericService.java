package me.seu.mybatis;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by liangfeihu on 2016/5/9.
 */
public interface IGenericService {

    <T> int insert(T model);

    <T> int update(T model);

    <T> int updateNoLock(T model);

    <T extends GenericModel> int remove(T model);

    <T> T getById(Class<T> modelClass, Long id);

    <T> int delete(Class modelClass, Long id);


    @Deprecated
    <T> List<T> listQueryPage(Condition condition, RowBounds rowBounds);

    @Deprecated
    <T> List<T> listQueryByCondition(Condition condition);

    @Deprecated
    int countByCondition(Condition condition);

    @Deprecated
    <T> T singleByCondition(Condition condition);

    @Deprecated
    <T> List<T> listQueryBySQL(String sqlCondition, Class<T> modelClass);

    @Deprecated
    <T> List<T> listQueryPage(String sqlCondition, Class<T> modelClass, RowBounds rowBounds);

    @Deprecated
    int countBySQL(String sqlCondition, Class modelClass);

    @Deprecated
    <T> T singleBySQL(String sqlCondition, Class<T> modelClass);

    @Deprecated
    <T> List<T> listQueryBySQL(String sqlCondition, String order, Class<T> modelClass);

    @Deprecated
    <T> List<T> listQueryPage(String sqlCondition, String order, Class<T> modelClass, RowBounds rowBounds);

    @Deprecated
    <T> T single(T model);

    @Deprecated
    <T> List<T> listQuery(T model);

    <T> List<T> listAll(Class modelClass);

    @Deprecated
    <T> int count(T model);

    @Deprecated
    <T> int updateByConditionSelective(Condition condition, T model);

    @Deprecated
    int deleteByCondition(Condition condition);

    @Deprecated
    int deleteBySQL(String sqlCondition, Class modelClass);

    <T> int insertList(Class<T> className, List<T> models);

    default String getBasePackage() {
        return null;
    }

    default String getInterface() {
        return null;
    }


    //新方法

    <T> List<T> listQueryBySQL(String sql, Class<T> modelClass, Object... params);

    <T> List<T> listPageBySQL(String sql, Class<T> modelClass, RowBounds rowBounds, Object... params);

    <T> T singleBySQL(String sql, Class<T> modelClass, Object... params);

    int countBySQL(String sql, Class modelClass, Object... params);


    /**
     * sql全部要自己写
     * 返回指定model （不能返回没有对应mapper的vo类似的model）
     * 支持关联查询
     */
    <T> List<T> nativeListQuery(String sql, Class<T> modelClass, Object... params);


    List<String> nativeListQueryReturnString(String sql, Object... params);

    List<Long> nativeListQueryReturnLong(String sql, Object... params);

    Integer nativeSingleQueryReturnInteger(String sql, Object... params);

    Object nativeSingleQueryReturnObject(String sql, Object... params);

    List<Object> nativeObjectListQuery(String sql, Object... params);

    List<Map<String, Object>> nativeMapListQuery(String sql, Object... params);

    int nativeUpdate(String sql, Object... params);
}
