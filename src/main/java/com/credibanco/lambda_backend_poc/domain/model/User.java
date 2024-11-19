package com.credibanco.lambda_backend_poc.domain.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String dateOfCreation;
}
