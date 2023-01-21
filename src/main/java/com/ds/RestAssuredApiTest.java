package com.ds;

import com.google.gson.Gson;
import com.ds.utils.GetExcelData;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredApiTest {

    Gson gson = new Gson();

    static {
        baseURI = "https://reqres.in/api";
    }

    @Test(testName = "Get User By Id", description = "Test get user by id for id 2")
    public void testGetUserById(){
        given().
                get("/users/2").
        then().
                statusCode(200).
                body("data.id",equalTo(2)).
                body("data.email",equalTo("janet.weaver@reqres.in")).
                body("data.first_name",equalTo("Janet")).
                body("data.last_name",equalTo("Weaver")).
                log().all(); // log the response;
    }

    @Test(testName = "Get List", description = "Test get users list in page 2")
    public void testGetUserList(){

        given().
                get("/users?page=2").
        then().
                statusCode(200).
                body("data[1].id",equalTo(8)).
                body("data[4].first_name",equalToIgnoringCase("George")).
                body("data.first_name",hasItems("George","Rachel")).
                log().all(); // log the response
    }

    @Test(testName = "Add User", description = "Test add new user")
    public void testCreateUser(){
        Map<String,String> user = new HashMap<>();
        user.put("name","Uthpala");
        user.put("job","SE");

        String userJson = gson.toJson(user);
        System.out.println(userJson);

        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                body(userJson).
        when().
                post("/api/users").
        then().
                statusCode(201).log().all();

    }

    @Test(testName = "Get User By Id", description = "Test get user by id by excel", dataProvider = "excel-data-user", dataProviderClass = GetExcelData.class)
    public void testGetUserByIdUsingExcelData(int id, String email, String firstName, String lastName){
        System.out.println("id = " + id + " email = "+email + " firstName = " + firstName + " lastName = " + lastName);
        given().
                get("/users/"+id).
                then().
                statusCode(200).
                body("data.id",equalTo(id)).
                body("data.email",equalTo(email)).
                body("data.first_name",equalTo(firstName)).
                body("data.last_name",equalTo(lastName)).
                log().all(); // log the response;
    }
}
