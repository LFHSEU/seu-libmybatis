package me.seu.mybatis;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by liangfeihu on 2016/5/9.
 */
public abstract class GenericService implements IGenericService {

    public abstract GenericDao getGenericDao();

    @Override
    public <T> int insert(T model) {
        return getGenericDao().insert(model);
    }

    @Override
    public <T> int update(T model) {
        return getGenericDao().update(model);
    }

    @Override
    public <T extends GenericModel> int remove(T model) {
        return getGenericDao().remove(model);
    }

    @Override
    public <T> int delete(Class modelClass, Long id) {
        return getGenericDao().delete(modelClass, id);
    }

    @Override
    public <T> T getById(Class<T> modelClass, Long id) {
        return getGenericDao().getById(modelClass, id);
    }

    @Override
    public <T> List<T> listQueryPage(Condition condition, RowBounds rowBounds) {
        return getGenericDao().listQueryPage(condition, rowBounds);
    }

    @Override
    public <T> List<T> listQueryByCondition(Condition condition) {
        return getGenericDao().listQueryByCondition(condition);
    }

    @Override
    public int countByCondition(Condition condition) {
        return getGenericDao().countByCondition(condition);
    }

    @Override
    public <T> T singleByCondition(Condition condition) {
        return getGenericDao().singleByCondition(condition);
    }

    @Override
    public <T> List<T> listQueryBySQL(String sqlCondition, Class<T> modelClass) {
        return getGenericDao().listQueryBySQL(sqlCondition, modelClass);
    }

    @Override
    public <T> int updateNoLock(T model) {
        return getGenericDao().updateNoLock(model);
    }

    @Override
    public <T> List<T> listQueryPage(String sqlCondition, Class<T> modelClass, RowBounds rowBounds) {
        return getGenericDao().listQueryPage(sqlCondition, modelClass, rowBounds);
    }

    @Override
    public int countBySQL(String sqlCondition, Class modelClass) {
        return getGenericDao().countBySQL(sqlCondition, modelClass);
    }

    @Override
    public <T> T singleBySQL(String sqlCondition, Class<T> modelClass) {
        return getGenericDao().singleBySQL(sqlCondition, modelClass);
    }

    @Override
    public <T> List<T> listQueryBySQL(String sqlCondition, String order, Class<T> modelClass) {
        return getGenericDao().listQueryBySQL(sqlCondition, order, modelClass);
    }

    @Override
    public <T> List<T> listQueryPage(String sqlCondition, String order, Class<T> modelClass, RowBounds rowBounds) {
        return getGenericDao().listQueryPage(sqlCondition, order, modelClass, rowBounds);
    }

    @Override
    public <T> T single(T model) {
        return getGenericDao().single(model);
    }

    @Override
    public <T> List<T> listQuery(T model) {
        return getGenericDao().listQuery(model);
    }

    @Override
    public <T> List<T> listAll(Class modelClass) {
        return getGenericDao().listAll(modelClass);
    }

    @Override
    public <T> int count(T model) {
        return getGenericDao().count(model);
    }

    @Override
    public <T> int updateByConditionSelective(Condition condition, T model) {
        return getGenericDao().updateByConditionSelective(condition, model);
    }

    @Override
    public <T> int insertList(Class<T> className, List<T> models) {
        return getGenericDao().insertList(className, models);
    }

    @Override
    public int deleteByCondition(Condition condition) {
        return getGenericDao().deleteByCondition(condition);
    }

    @Override
    public int deleteBySQL(String sqlCondition, Class modelClass) {
        return getGenericDao().deleteBySQL(sqlCondition, modelClass);
    }

    @Override
    public <T> List<T> listQueryBySQL(String sql, Class<T> modelClass, Object... params) {
        return getGenericDao().listQueryBySQL(sql, modelClass, params);
    }

    @Override
    public <T> List<T> listPageBySQL(String sql, Class<T> modelClass, RowBounds rowBounds, Object... params) {
        return getGenericDao().listPageBySQL(sql, modelClass, rowBounds, params);
    }

    @Override
    public <T> T singleBySQL(String sql, Class<T> modelClass, Object... params) {
        return getGenericDao().singleBySQL(sql, modelClass, params);
    }

    @Override
    public int countBySQL(String sql, Class modelClass, Object... params) {
        return getGenericDao().countBySQL(sql, modelClass, params);
    }

    @Override
    public <T> List<T> nativeListQuery(String sql, Class<T> modelClass, Object... params) {
        return getGenericDao().nativeListQuery(sql, modelClass, params);
    }

    @Override
    public List<String> nativeListQueryReturnString(String sql, Object... params) {
        return getGenericDao().nativeListQueryReturnString(sql, params);
    }

    @Override
    public List<Long> nativeListQueryReturnLong(String sql, Object... params) {
        return getGenericDao().nativeListQueryReturnLong(sql, params);
    }

    @Override
    public Integer nativeSingleQueryReturnInteger(String sql, Object... params) {
        return getGenericDao().nativeSingleQueryReturnInteger(sql, params);
    }

    @Override
    public Object nativeSingleQueryReturnObject(String sql, Object... params) {
        return getGenericDao().nativeSingleQueryReturnObject(sql, params);
    }

    @Override
    public List<Object> nativeObjectListQuery(String sql, Object... params) {
        return getGenericDao().nativeObjectListQuery(sql, params);
    }

    @Override
    public List<Map<String, Object>> nativeMapListQuery(String sql, Object... params) {
        return getGenericDao().nativeMapListQuery(sql, params);
    }

    @Override
    public int nativeUpdate(String sql, Object... params) {
        return getGenericDao().nativeUpdate(sql, params);
    }
}
