package com.clothes.noc.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    @Builder.Default
    String lastname = "";
    @Builder.Default
    String firstname = "";
    @Builder.Default
    String avatar = "";
}
