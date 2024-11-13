package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class ContactForm {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String address;

    private boolean favourite;

    private String websiteLink;

    private String linkedinLink;

    @ValidFile(message = "Valid image is required")
    private MultipartFile contactImage;

    private String picture;
}
