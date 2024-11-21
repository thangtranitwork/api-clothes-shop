package com.clothes.noc.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class) // Liên kết với validator logic
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Áp dụng cho trường và tham số
@Retention(RetentionPolicy.RUNTIME) // Lưu annotation ở thời gian runtime
public @interface Password {

    String message() default "PASSWORD_IS_INVALID"; // Thông báo mặc định

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

