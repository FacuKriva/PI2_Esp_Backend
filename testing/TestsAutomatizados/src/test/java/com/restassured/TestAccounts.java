package com.restassured;

import com.restassured.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

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

}
