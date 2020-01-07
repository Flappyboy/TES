package top.jach.tes.core.simple.domain.action;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InfoParam {

    String infoName() default "";
}
