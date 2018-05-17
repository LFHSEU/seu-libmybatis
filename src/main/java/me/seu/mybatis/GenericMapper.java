package me.seu.mybatis;

import me.seu.mybatis.mapper.SelectSQLMapper;
import me.seu.mybatis.mapper.SingleByConditionMapper;
import me.seu.mybatis.mapper.UpdateSQLMapper;
import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.condition.*;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * Created by liangfeihu on 2016/6/16.
 */
public interface GenericMapper<T> extends
        InsertMapper<T>,
        InsertListMapper<T>,
        SelectByPrimaryKeyMapper<T>,
        SelectMapper<T>,
        SelectOneMapper<T>,
        SelectCountMapper<T>,
        UpdateSQLMapper<T>,
        UpdateByPrimaryKeyMapper<T>,
        UpdateByConditionMapper<T>,
        UpdateByConditionSelectiveMapper<T>,
        DeleteByPrimaryKeyMapper<T>,
        DeleteByConditionMapper<T>,
        SelectByConditionMapper<T>,
        SelectByConditionRowBoundsMapper<T>,
        SelectCountByConditionMapper<T>,
        SingleByConditionMapper<T>,
        SelectSQLMapper<T>,
        Marker {

    public final static String className = "me.seu.mybatis.GenericMapper";

}
