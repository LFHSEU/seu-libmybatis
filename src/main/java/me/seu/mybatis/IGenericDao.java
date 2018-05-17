package me.seu.mybatis;

/**
 * Created by liangfeihu on 2016/11/14.
 */
public interface IGenericDao {

    default String getBasePackage() {
        return null;
    }

    default String getInterface() {
        return "me.seu.mybatis.GenericMapper";
    }

    default String getMapperFullClassName(Class modelClass) {
        return null;
    }
}
