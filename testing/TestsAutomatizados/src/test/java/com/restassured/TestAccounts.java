package com.restassured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestAccounts extends Variables {

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

    //TC_Cuenta_0001
    @Tag("Smoke")
    @Test
    public void ViewAccountSuccess200() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("alias"))
                    .body("alias", Matchers.equalTo("afectacion.divisa.cambios"))
                    .body("$",hasKey("cvu"))
                    .body("cvu", Matchers.equalTo("1828142364587587493333"))
                    .body("$",hasKey("availableBalance"))
                    .body("availableBalance", Matchers.equalTo(220000f))
                    .body("$",hasKey("account_id"))
                    .body("account_id", Matchers.equalTo(2))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0002
    @Tag("Smoke")
    @Test
    public void ViewAccountFailure404() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 999).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0003
    @Tag("Smoke")
    @Test
    public void ViewAccountFailure401() {

        Response response;

        response = given()
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(401)
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0004
    @Tag("Smoke")
    @Test
    public void UpdateAccountSuccess200() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "Ciruela");
        request.put("word_index_one", "Botella");
        request.put("word_index_two", "Merengue");

        RestAssured.registerParser("text/plain", Parser.TEXT);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.TEXT)
                    .body(equalTo("New Alias: ciruela.botella.merengue"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0005
    @Tag("Smoke")
    @Test
    public void UpdateAccountFailure404() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "Margarita");
        request.put("word_index_one", "Arroyo");
        request.put("word_index_two", "Sombrilla");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 99)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0006
    @Tag("Smoke")
    @Test
    public void UpdateAccountFailure401() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "Manzana");
        request.put("word_index_one", "Perejil");
        request.put("word_index_two", "Mariposa");

        response = given()
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(401)
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0007
    @Tag("Smoke")
    @Test
    public void UpdateAccountFailure409() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "Riqueza");
        request.put("word_index_one", "Dineros");
        request.put("word_index_two", "Devaluacion");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(409)
                    .statusCode(HttpStatus.SC_CONFLICT)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The alias is already registered"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0008
    @Tag("Smoke")
    @Test
    public void UpdateAccountFailure400AliasEmptyWord() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "");
        request.put("word_index_one", "Dineros");
        request.put("word_index_two", "Devaluacion");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("You must choose 3 words. Words cannot be blank."))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Cuenta_0009
    @Tag("Smoke")
    @Test
    public void UpdateAccountFailure400AliasRepeatedWord() {

        Response response;

        JSONObject request = new JSONObject();
        request.put("word_index_zero", "Dineros");
        request.put("word_index_one", "Dineros");
        request.put("word_index_two", "Devaluacion");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("All the words must be different."))
                    .log().all()
                    .extract()
                    .response();

    }

}
