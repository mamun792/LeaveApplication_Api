package com.example.leave_app.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(name = "bearerAuth", description = "JWT Token", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}