package me.seu.mybatis;

import lombok.extern.slf4j.Slf4j;
import me.seu.mybatis.exception.ObjectOptimisticLockingFailureException;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created liangfeihu on 2016/5/8.
 */
@Slf4j
public abstract class GenericDao implements IGenericDao {
    protected Map<String, Class> mapperMap;

    abstract protected SqlSession getSession();


    public <T> T getMapper(Class<T> modelClass) {
        return getSession().getMapper(modelClass);
    }

    protected <T extends GenericMapper> Class<T> getMapperClass(Class modelClass) {
        try {
            if (mapperMap == null && getInterface() != null && getBasePackage() != null) {
                MapperClassPathScanningProvider mapperClassPathScanningProvider = new MapperClassPathScanningProvider();
                mapperMap = mapperClassPathScanningProvider.findMapper(getBasePackage(), getInterface());
            }
            if (mapperMap != null && mapperMap.containsKey(modelClass.getName())) {
                return mapperMap.get(modelClass.getName());
            }

            Class mapperClass = Class.forName(getMapperFullClassName(modelClass));
            return mapperClass;
        } catch (Exception e) {
            return null;
        }
    }

    protected <T extends GenericMapper> Class<T> getFirstMapperClass() {
        try {
            if (mapperMap == null && getInterface() != null && getBasePackage() != null) {
                MapperClassPathScanningProvider mapperClassPathScanningProvider = new MapperClassPathScanningProvider();
                mapperMap = mapperClassPathScanningProvider.findMapper(getBasePackage(), getInterface());
            }
            return (Class<T>) mapperMap.values().toArray()[0];
        } catch (Exception e) {
            return null;
        }
    }


    protected GenericMapper getModelMapper(Class modelClass) {
        return getSession().getMapper(getMapperClass(modelClass));
    }

    private void updateVersion(Object model) {
        try {
            if (model instanceof GenericModel) {
                GenericModel genericModel = (GenericModel) model;
                genericModel.setVersion(genericModel.getVersion() + 1);
                genericModel.setUpdated(new Date());
            }

            if (model instanceof SequenceModel) {
                SequenceModel sequenceModel = (SequenceModel) model;
                sequenceModel.setVersion(sequenceModel.getVersion() + 1);
                sequenceModel.setUpdated(new Date());
            }
        } catch (Exception e) {
        }
    }

    private void setDeleted(Object model) {
        try {
            if (model instanceof GenericModel) {
                GenericModel genericModel = (GenericModel) model;
                genericModel.setDeleted(new Date());
                genericModel.setUpdated(new Date());
                genericModel.setVersion(genericModel.getVersion() + 1);
            }
            if (model instanceof SequenceModel) {
                SequenceModel sequenceModel = (SequenceModel) model;
                sequenceModel.setDeleted(new Date());
                sequenceModel.setUpdated(new Date());
                sequenceModel.setVersion(sequenceModel.getVersion() + 1);
            }
        } catch (Exception e) {
        }
    }

    private void setAllBaseFieldsDefault(Object model) {
        try {
            if (model instanceof GenericReadOnlyModel) {
                GenericReadOnlyModel genericReadOnlyModel = (GenericReadOnlyModel) model;
                if (null == genericReadOnlyModel.getCreated()) {
                    genericReadOnlyModel.setCreated(new Date());
                }
            }
            if (model instanceof GenericModel) {
                GenericModel genericModel = (GenericModel) model;
                genericModel.setUpdated(new Date());
                genericModel.setVersion(0);
            }

            if (model instanceof SequenceReadOnlyModel) {
                SequenceReadOnlyModel sequenceReadOnlyModel = (SequenceReadOnlyModel) model;
                sequenceReadOnlyModel.setCreated(new Date());
            }
            if (model instanceof SequenceModel) {
                SequenceModel sequenceModel = (SequenceModel) model;
                sequenceModel.setUpdated(new Date());
                sequenceModel.setVersion(0);
            }
        } catch (Exception e) {
        }
    }

    //保存一个实体，null的属性也会保存，不会使用数据库默认值
    public <T> int insert(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        setAllBaseFieldsDefault(model);
        int num = mapper.insert(model);
        if (model instanceof GenericReadOnlyModel) {
            log.info("insert {} id = {}", model.getClass().getName(), ((GenericReadOnlyModel) model).getId());
        }
        return num;
    }

    //根据主键更新实体全部字段
    public <T> int update(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        updateVersion(model);
        int num = mapper.update(model);
        if (num == 0) {
            if (model instanceof GenericModel) {
                GenericModel genericModel = (GenericModel) model;
                throw new ObjectOptimisticLockingFailureException("update [" + model.getClass().getName() + "] id = " + genericModel.getId() + " version = " + (genericModel.getVersion() - 1) + " optimistic locking error  (or not find id)");
            }
        }
//        if (model instanceof GenericModel) {
//            Condition condition = new Condition(model.getClass());
//            GenericModel genericModel = (GenericModel) model;
//            condition.createCriteria()
//                    .andEqualTo("id", genericModel.getId())
//                    .andCondition("version = " + (genericModel.getVersion() - 1));
//            num = mapper.updateByCondition(model, condition);
//            if (num == 0) {
//                throw new ObjectOptimisticLockingFailureException("update [" + model.getClass().getName() + "] id = " + genericModel.getId() + " optimistic locking error  (or not find id)");
//            }
//        } else {
//            num = mapper.updateByPrimaryKey(model);
//        }
        if (model instanceof GenericModel) {
            log.info("updated {} id = {} version = {} num = {}", model.getClass().getName(), ((GenericModel) model).getId(), ((GenericModel) model).getVersion(), num);
        } else if (model instanceof GenericReadOnlyModel) {
            log.info("updated {} id = {} num = {}", model.getClass().getName(), ((GenericReadOnlyModel) model).getId(), num);
        }
        return num;
    }

    public <T> int updateNoLock(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        updateVersion(model);
        int num = mapper.updateByPrimaryKey(model);
        if (model instanceof GenericModel) {
            log.info("updated {} id = {} version = {}", model.getClass().getName(), ((GenericModel) model).getId(), ((GenericModel) model).getVersion());
        } else if (model instanceof GenericReadOnlyModel) {
            log.info("updated {} id = {}", model.getClass().getName(), ((GenericReadOnlyModel) model).getId());
        }
        return num;
    }

    //根据主键逻辑删除
    public <T extends GenericModel> int remove(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        setDeleted(model);
        int num = mapper.updateByPrimaryKey(model);
        if (model instanceof GenericReadOnlyModel) {
            log.info("removed {} id = {}", model.getClass().getName(), ((GenericReadOnlyModel) model).getId());
        }
        return num;
    }

    public <T> int delete(Class modelClass, Long id) {
        GenericMapper<T> mapper = getModelMapper(modelClass);
        int num = mapper.deleteByPrimaryKey(id);
        log.info("removed {} id = {}", modelClass.getName(), id);
        return num;
    }

    //根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
    public <T> T getById(Class<T> modelClass, Long id) {
        GenericMapper<T> mapper = getModelMapper(modelClass);
        T model = mapper.selectByPrimaryKey(id);
        return model;
    }

    @Deprecated
    public <T> List<T> listQueryByCondition(Condition condition) {
        ;
        GenericMapper<T> mapper = getModelMapper(condition.getEntityClass());
        List<T> ls = mapper.selectByCondition(condition);
        return ls;
    }


    public <T> List<T> listQueryBySQL(String sqlCondition, Class<T> modelClass) {
        String sql = sqlCondition.trim();
        GenericMapper mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", getSql(sqlCondition));
        return mapper.listQueryBySQL(paramsMap);
//        Condition condition = new Condition(modelClass);
//        condition.createCriteria().andCondition(sqlCondition);
//        return listQueryByCondition(condition);
    }

    private String getSql(String sqlCon) {
        String sql = sqlCon.trim();
        if (sql.length() <= 6) {
            return "WHERE " + sql;
        }
        String where = sql.substring(0, 6);
        if ("WHERE ".equals(where.toUpperCase())) {
            return sql;
        }
        String orderBy = sql.substring(0, 9);
        if ("ORDER BY ".equals(orderBy.toUpperCase())) {
            return sql;
        }
        sql = "WHERE " + sql;
        return sql;
    }

    @Deprecated
    public <T> List<T> listQueryBySQL(String sqlCondition, String order, Class<T> modelClass) {
        Condition condition = new Condition(modelClass);
        Example.Criteria criteria = condition.createCriteria().andCondition(sqlCondition);
        if (!StringUtils.isEmpty(order)) {
            condition.setOrderByClause(order);
        }
        return listQueryByCondition(condition);
    }

    @Deprecated
    public <T> List<T> listQueryPage(Condition condition, RowBounds rowBounds) {
        GenericMapper<T> mapper = getModelMapper(condition.getEntityClass());
        List<T> ls = mapper.selectByConditionAndRowBounds(condition, rowBounds);
        return ls;
    }

    @Deprecated
    public <T> List<T> listQueryPage(String sqlCondition, Class<T> modelClass, RowBounds rowBounds) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andCondition(sqlCondition);
        return listQueryPage(condition, rowBounds);
    }

    @Deprecated
    public <T> List<T> listQueryPage(String sqlCondition, String order, Class<T> modelClass, RowBounds rowBounds) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andCondition(sqlCondition);
        if (!StringUtils.isEmpty(order)) {
            condition.setOrderByClause(order);
        }
        return listQueryPage(condition, rowBounds);
    }

    @Deprecated
    public int countByCondition(Condition condition) {
        GenericMapper mapper = getModelMapper(condition.getEntityClass());
        int count = mapper.selectCountByCondition(condition);
        return count;
    }

    @Deprecated
    public int countBySQL(String sqlCondition, Class modelClass) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andCondition(sqlCondition);
        return countByCondition(condition);
    }

    @Deprecated
    public <T> T singleByCondition(Condition condition) {
        GenericMapper<T> mapper = getModelMapper(condition.getEntityClass());
        T single = mapper.singleByCondition(condition);
        return single;
    }

    @Deprecated
    public <T> T singleBySQL(String sqlCondition, Class<T> modelClass) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andCondition(sqlCondition);
        return singleByCondition(condition);
    }

    @Deprecated
    public <T> T single(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        T single = mapper.selectOne(model);
        return single;
    }

    @Deprecated
    public <T> List<T> listQuery(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        List<T> ls = mapper.select(model);
        return ls;
    }

    public <T> List<T> listAll(Class modelClass) {
        GenericMapper<T> mapper = getModelMapper(modelClass);
        List<T> ls = mapper.select(null);
        return ls;
    }

    @Deprecated
    public <T> int count(T model) {
        GenericMapper<T> mapper = getModelMapper(model.getClass());
        int num = mapper.selectCount(model);
        return num;
    }

    @Deprecated
    public <T> int updateByConditionSelective(Condition condition, T model) {
        GenericMapper mapper = getModelMapper(condition.getEntityClass());
        return mapper.updateByConditionSelective(model, condition);
    }

    public <T> int insertList(Class<T> className, List<T> models) {
        for (T model : models) {
            setAllBaseFieldsDefault(model);
        }
        GenericMapper<T> mapper = getModelMapper(className);
        return mapper.insertList(models);
    }

    @Deprecated
    public int deleteByCondition(Condition condition) {
        GenericMapper mapper = getModelMapper(condition.getEntityClass());
        return mapper.deleteByCondition(condition);
    }

    @Deprecated
    public int deleteBySQL(String sqlCondition, Class modelClass) {
        Condition condition = new Condition(modelClass);
        condition.createCriteria().andCondition(sqlCondition);
        return deleteByCondition(condition);
    }


    public <T> List<T> listQueryBySQL(String sql, Class<T> modelClass, Object... params) {
        GenericMapper mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.listQueryBySQL(paramsMap);
    }

    public <T> List<T> listPageBySQL(String sql, Class<T> modelClass, RowBounds rowBounds, Object... params) {
        GenericMapper mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.listPageBySQL(paramsMap, rowBounds);
    }

    public <T> T singleBySQL(String sql, Class<T> modelClass, Object... params) {
        GenericMapper<T> mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.singleBySQL(paramsMap);
    }

    public int countBySQL(String sql, Class modelClass, Object... params) {
        GenericMapper mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.countBySQL(paramsMap);
    }

    public <T> List<T> nativeListQuery(String sql, Class<T> modelClass, Object... params) {
        GenericMapper mapper = getModelMapper(modelClass);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeListQuery(paramsMap);
    }


    public <T extends GenericMapper> List<String> nativeListQueryReturnString(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeListQueryReturnString(paramsMap);
    }

    public <T extends GenericMapper> List<Long> nativeListQueryReturnLong(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeListQueryReturnLong(paramsMap);
    }

    public <T extends GenericMapper> Integer nativeSingleQueryReturnInteger(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeSingleQueryReturnInteger(paramsMap);
    }

    public <T extends GenericMapper> Object nativeSingleQueryReturnObject(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeSingleQueryReturnObject(paramsMap);
    }


    public <T extends GenericMapper> List<Object> nativeObjectListQuery(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeObjectListQuery(paramsMap);
    }

    public <T extends GenericMapper> List<Map<String, Object>> nativeMapListQuery(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeMapListQuery(paramsMap);
    }

    public <T extends GenericMapper> int nativeUpdate(String sql, Object... params) {
        GenericMapper mapper = getSession().getMapper(getFirstMapperClass());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("sql", sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] != null) {
                    paramsMap.put(i + "", params[i]);
                }
            }
        }
        return mapper.nativeUpdate(paramsMap);
    }
}
