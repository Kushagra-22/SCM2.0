package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5; // 5mb

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File cannot be empty or nulll").addConstraintViolation();
            return false;
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should be less than 5mb").addConstraintViolation();
            return false;
        }
        // BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        // if(bufferedImage.getWidth())
        else {
            return true;
        }

    }

}
