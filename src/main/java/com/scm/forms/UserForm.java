package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserForm {
    @NotBlank(message = "Name is required")
    @Size(min=3,message = "Min 3 characters required")
    private String name;
    @NotBlank
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank 
    @Size(min = 6)
    private String password;
    @NotBlank
    private String about;
    @NotBlank
    private String phoneNumber;
}
