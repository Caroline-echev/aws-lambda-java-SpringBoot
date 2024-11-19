package com.credibanco.lambda_backend_poc.infraestructure.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String lastName;
    private String email;
}
