package com.nagarro.driven.base;

import com.nagarro.driven.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiBase {

    protected RequestSpecification request;

    public ApiBase() {
        RestAssured.baseURI = ConfigManager.get("api.base.url");

        request = RestAssured.given()
                .contentType("application/json")
                .accept("application/json")
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public Response get(String endpoint) {
        return request.when().get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return request.body(body).when().post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return request.body(body).when().put(endpoint);
    }

    public Response delete(String endpoint) {
        return request.when().delete(endpoint);
    }

    public void addAuthToken(String token) {
        request.header("Authorization", "Bearer " + token);
    }
}
