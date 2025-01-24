package com.clothes.noc.dto.request;

import com.clothes.noc.enums.Platform;
import com.clothes.noc.validator.Password;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
    @Password
    String password;
    String firstname;
    String lastname;
    @Builder.Default
    String platform = Platform.APP.name();
}
