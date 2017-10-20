package com.easted.sy.user.archieves.uaa.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileNotEmptyValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileNotEmptyConstraint {
    String message() default "File Not Empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
