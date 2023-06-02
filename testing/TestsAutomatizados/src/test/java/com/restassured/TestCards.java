package com.restassured;

import com.restassured.model.Card;
import com.restassured.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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

    //TC_Tarjetas_0001
    @Tag("Smoke")
    @Test
    public void ViewAllCardsSuccess200() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(List.class))
                    .body("[0]",hasKey("card_id"))
                    .body("[0]",hasKey("alias"))
                    .body("[0]",hasKey("cardNumber"))
                    .body("[0]",hasKey("cardHolder"))
                    .body("[0]",hasKey("expirationDate"))
                    .body("[0]",hasKey("bank"))
                    .body("[0]",hasKey("cardType"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tajetas_0002
    @Tag("Smoke")
    @Test
    public void ViewAllCardsSuccess204() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 4).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(204)
                    .statusCode(HttpStatus.SC_NO_CONTENT)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0003
    @Tag("Smoke")
    @Test
    public void ViewAllCardsFailure401() {

        Response response;

        response = given()
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 1).
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

    //TC_Tarjetas_0004
    @Tag("Smoke")
    @Test
    public void ViewAllCardsFailure404() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 99).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Tarjetas_0005
    @Tag("Smoke")
    @Test
    public void ViewACardSuccess200() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("card_id"))
                    .body("$",hasKey("alias"))
                    .body("$",hasKey("cardNumber"))
                    .body("$",hasKey("cardHolder"))
                    .body("$",hasKey("expirationDate"))
                    .body("$",hasKey("bank"))
                    .body("$",hasKey("cardType"))
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Tarjetas_0006
    @Tag("Smoke")
    @Test
    public void ViewACardFailure401() {

        Response response;

        response = given()
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 1).
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


    //TC_Tarjetas_0008
    @Tag("Smoke")
    @Test
    public void ViewACardFailure404AccountNotFound() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 99)
                    .pathParams("idCard", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Tarjetas_0009
    @Tag("Smoke")
    @Test
    public void ViewACardFailure404CardNotFound() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 99).
                when().
                    get().
                    then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tajetas_0010
    @Tag("Smoke")
    @Test
    public void AddCardSuccess200() {

        Response response;

        Card card = new Card("DH Money", 1234717505193642L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 1)
                    .body(card).
                when().
                    post().
                then()
                .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("card_id"))
                    .body("$",hasKey("alias"))
                    .body("alias", Matchers.equalTo("DH Money"))
                    .body("$",hasKey("cardNumber"))
                    .body("cardNumber", Matchers.equalTo(1234717505193642L))
                    .body("$",hasKey("cardHolder"))
                    .body("cardHolder", Matchers.equalTo("Juan Pedro Perez"))
                    .body("$",hasKey("expirationDate"))
                    .body("expirationDate", Matchers.equalTo("08/2026"))
                    .body("$",hasKey("bank"))
                    .body("bank", Matchers.equalTo("Digital Money Bank"))
                    .body("$",hasKey("cardType"))
                    .body("cardType", Matchers.equalTo("Debito"))
                    .body("$", not(hasKey("cvv")))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tajetas_0011
    @Tag("Smoke")
    @Test
    public void AddCardFailure409() {

        Response response;

        Card card = new Card("DH Money", 1234717505193642L, "Martina Zeta", "08/2026", 827, "Digital Money Bank", "Debito");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 2)
                    .body(card).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(409)
                    .statusCode(HttpStatus.SC_CONFLICT)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Already Registered"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tajetas_0012
    @Tag("Smoke")
    @Test
    public void AddCardFailure404() {

        Response response;

        Card card = new Card("DH Money", 1234717505193642L, "Martina Zeta", "08/2026", 827, "Digital Money Bank", "Debito");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 99)
                    .body(card).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }



    //TC_Tajetas_0013
    @Tag("Smoke")
    @Test
    public void AddCardFailure401() {

        Response response;

        Card card = new Card("DH Money", 1234717505193642L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

        response = given()
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards")
                    .pathParams("id", 1)
                    .body(card).
                when().
                    post().
                then()
                        .assertThat()
                        .statusCode(401)
                        .statusCode(HttpStatus.SC_UNAUTHORIZED)
                        .log().all()
                        .extract()
                        .response();

    }



}
