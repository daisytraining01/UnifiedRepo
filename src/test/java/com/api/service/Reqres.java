package com.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maveric.core.utils.reporter.Report;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

public class Reqres {

    public void getUserDetailsTest() {
        Report.log("this is a ger user test");
        int statusCode;
        RestAssured.baseURI = "https://reqres.in/api/users/";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET, "1");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);

    }

    public void createUserTest() {
        Report.log("this is a create user test");
        int statusCode;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode data = mapper.createObjectNode();
        data.put("name", "Aadya");
        data.put("job", "student");
        RestAssured.baseURI = "https://reqres.in/api/users/";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(data.toPrettyString());
        Response response = httpRequest.request(Method.POST);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);

    }


    public void updateUserDetailsTest() {
        Report.log("this is a update user test");
        int statusCode;
        RestAssured.baseURI = "https://reqres.in/api/users/";
        RequestSpecification httpRequest = RestAssured.given();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode updateData = mapper.createObjectNode();
        updateData.put("name", "Aarna");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(updateData.toPrettyString());
        Response response = httpRequest.request(Method.PUT, "4");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        JsonPath newData = response.jsonPath();
        String name = newData.get("name");
        Assert.assertEquals(name, "Aarna");

    }

    public void deleteUserTest() {
        Report.log("this is a delete user test");
        int statusCode;
        RestAssured.baseURI = "https://reqres.in/api/users/";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.DELETE, "5");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 204);
    }
}
