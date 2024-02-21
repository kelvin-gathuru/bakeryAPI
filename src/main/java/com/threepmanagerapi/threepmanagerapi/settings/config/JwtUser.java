package com.threepmanagerapi.threepmanagerapi.settings.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {
    private Long userID;
    private String email;
    private String name;
}
