package me.seu.mybatis.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.entity.Condition;

/**
 * Created liangfeihu on 2016/6/17.
 */
public interface SingleByConditionMapper<T> {
    @SelectProvider(type = GenericProvider.class, method = "dynamicSQL")
    T singleByCondition(Condition condition);
}
