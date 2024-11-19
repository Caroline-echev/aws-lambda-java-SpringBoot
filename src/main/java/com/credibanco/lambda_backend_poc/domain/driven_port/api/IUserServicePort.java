package com.credibanco.lambda_backend_poc.domain.driven_port.api;


import com.credibanco.lambda_backend_poc.domain.model.User;

public interface IUserServicePort {
    User save(String name, String lastName, String email);
}
