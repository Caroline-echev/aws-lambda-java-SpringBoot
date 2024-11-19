package com.credibanco.lambda_backend_poc.infraestructure.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.credibanco.lambda_backend_poc.domain.driven_port.api.IUserServicePort;
import com.credibanco.lambda_backend_poc.domain.model.User;
import com.credibanco.lambda_backend_poc.domain.use_case.UserUseCase;
import com.credibanco.lambda_backend_poc.infraestructure.dto.UserRequest;
import com.credibanco.lambda_backend_poc.infraestructure.exception.Error;
import com.credibanco.lambda_backend_poc.infraestructure.exception.ResponseError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.credibanco.lambda_backend_poc.infraestructure.util.Constants.*;

public class CreateUserLambda  implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final Gson gson;
    private final IUserServicePort userServicePort;

    public CreateUserLambda() {
        this.userServicePort = new UserUseCase();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        final LambdaLogger logger = context.getLogger();
        logger.log(TITLE_REQUEST + event.getBody());

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            UserRequest input = gson.fromJson(event.getBody(), UserRequest.class);

            if (input == null || input.getEmail() == null || input.getName() == null || input.getLastName() == null
                   ) {
                return returnApiResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, REQUEST_BODY_INTERNAL_SERVER_ERROR,
                        ERROR_MESSAGE_INTERNAL_SERVER_ERROR, logger);
            }
            if( input.getEmail().isEmpty() || input.getName().isEmpty() || input.getLastName().isEmpty()){
                return returnApiResponse(HttpStatus.SC_BAD_REQUEST, REQUEST_BODY_BAD_REQUEST,
                        ERROR_MESSAGE_BAD_REQUEST, logger);
            }

            User user = userServicePort.save(input.getName(), input.getLastName(), input.getEmail());

            response.setStatusCode(HttpStatus.SC_OK);
            response.setHeaders(getDefaultHeaders());
            response.setBody(gson.toJson(user));
        } catch (Exception e) {
            logger.log(TITLE_ERROR + e.getMessage());
            return returnApiResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, REQUEST_BODY_INTERNAL_SERVER_ERROR,
                    e.getMessage(), logger);
        }

        return response;
    }

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, APPLICATION_JSON );
        return headers;
    }

    public APIGatewayProxyResponseEvent returnApiResponse(int statusCode, String responseBody,
                                                          String errorMessage, LambdaLogger logger) {
        final Error error = new Error();
        error.setErrorMessage(errorMessage);

        ResponseError<String> response = new ResponseError<>(statusCode, responseBody, error);

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(getDefaultHeaders())
                .withBody(gson.toJson(response));

        logger.log(TITLE_RESPONSE + responseEvent);
        return responseEvent;
    }
}