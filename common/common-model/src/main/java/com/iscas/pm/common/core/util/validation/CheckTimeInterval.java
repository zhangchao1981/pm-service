package com.iscas.pm.common.core.util.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;


import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 自定义validation实现开始时间不能大于结束时间
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckTimeInterval.CheckTimeIntervalValidation.class)
@Documented
@Repeatable(CheckTimeInterval.List.class)
public @interface CheckTimeInterval {

    String[] beginTime() default {"beginTime"};

    String[] endTime() default {"endTime"};

    String message() default "开始时间不能大于结束时间 ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CheckTimeInterval[] value();
    }

    class CheckTimeIntervalValidation implements ConstraintValidator<CheckTimeInterval, Object> {

        private String[] beginTime;
        private String[] endTime;

        @Override
        public void initialize(CheckTimeInterval constraintAnnotation) {
            this.beginTime = constraintAnnotation.beginTime();
            this.endTime = constraintAnnotation.endTime();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(value);
            boolean valid = true;
            for (int i = 0; i < beginTime.length; i++) {
                String s = beginTime[i];
                Object propertyValue = beanWrapper.getPropertyValue(s);
                Object propertyValue1 = beanWrapper.getPropertyValue(endTime[i]);
                if (!ObjectUtils.isEmpty(propertyValue) && !ObjectUtils.isEmpty(propertyValue1)) {
                    Date beginTimeVal = (Date) propertyValue;
                    Date endTimeVal = (Date) propertyValue1;
                    int result = endTimeVal.compareTo(beginTimeVal);
                    if (result < 0) {
                        valid = false;
                        break;
                    }
                }
            }
            return valid;
        }
    }
}
