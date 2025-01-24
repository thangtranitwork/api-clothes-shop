package com.clothes.noc.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.clothes.noc.enums.Platform;
import com.clothes.noc.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2RegisterRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
    @Builder.Default
    String roles = Role.USER.name();
    @Builder.Default
    String platform = Platform.APP.name();
    String lastname;
    String firstname;
}
