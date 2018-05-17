package me.seu.mybatis.mapper;

import org.apache.ibatis.annotations.UpdateProvider;

public interface UpdateSQLMapper<T> {
    @UpdateProvider(
            type = UpdateSQLProvider.class,
            method = "dynamicSQL"
    )
    int update(T model);
}
