package me.seu.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EnableAutoGenMapper {

    String[] modelPackageName();

    String mapperPackageName();

    String mapperPrefix() default "";

    String superMapperClassName();
}
