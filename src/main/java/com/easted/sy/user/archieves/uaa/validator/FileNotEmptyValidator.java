package com.easted.sy.user.archieves.uaa.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmptyConstraint,MultipartFile> {
    @Override
    public void initialize(FileNotEmptyConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }


}
