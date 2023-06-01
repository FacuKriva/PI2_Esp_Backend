package com.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class TestCards extends Variables {

    private static String token;


    @BeforeAll
    public static void  Setup() {

        RestAssured.baseURI = base_uri;
    }

    @BeforeAll
    public static void Login(){
        token = given()
                    .auth().preemptive()
                    .basic(client_id, client_secret)
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "password")
                    .formParam("username", username_accounts)
                    .formParam("password", password_accounts)
                    .basePath("/security/oauth/token")
                .when()
                    .post()
                .then()
                    .log().all()
                    .extract()
                    .jsonPath().get("access_token");
    }

}
