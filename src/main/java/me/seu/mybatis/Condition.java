package me.seu.mybatis;

/**
 * Created liangfeihu on 2016/7/7.
 */
public class Condition extends tk.mybatis.mapper.entity.Condition {

//    private String selectCustomColumns;

    public Condition(Class<?> entityClass) {
        super(entityClass);
    }

    public Condition(Class<?> entityClass, boolean exists) {
        super(entityClass, exists);
    }

    public Condition(Class<?> entityClass, boolean exists, boolean notNull) {
        super(entityClass, exists, notNull);
    }

//    public String getSelectCustomColumns() {
//        return selectCustomColumns;
//    }
//
//    public void setSelectCustomColumns(String selectCustomColumns) {
//        this.selectCustomColumns = selectCustomColumns;
//    }
}
