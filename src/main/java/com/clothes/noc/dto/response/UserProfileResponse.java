package com.clothes.noc.dto.response;

import com.clothes.noc.enums.Platform;
import com.clothes.noc.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String lastname;
    String firstname;
    String email;
    Platform platform;
    Role role;
}
