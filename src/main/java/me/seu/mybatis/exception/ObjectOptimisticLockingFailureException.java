package me.seu.mybatis.exception;

/**
 * Created by liangfeihu on 2017/3/11.
 */
public class ObjectOptimisticLockingFailureException extends GenericMapperException {
    public ObjectOptimisticLockingFailureException(String message) {
        super(message);
    }
}
