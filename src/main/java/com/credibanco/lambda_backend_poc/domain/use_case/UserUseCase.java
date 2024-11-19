package com.credibanco.lambda_backend_poc.domain.use_case;

import com.credibanco.lambda_backend_poc.domain.driven_port.api.IUserServicePort;
import com.credibanco.lambda_backend_poc.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserUseCase implements IUserServicePort {


    @Override
    public User save(String name, String lastName, String email) {
        User userSaved = new User();
        userSaved.setId(UUID.randomUUID().toString());
        userSaved.setName(name);
        userSaved.setLastName(lastName);
        userSaved.setEmail(email);
        userSaved.setDateOfCreation(LocalDateTime.now().toString());
        return userSaved;
    }

}
