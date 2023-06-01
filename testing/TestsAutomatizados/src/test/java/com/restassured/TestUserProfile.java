package com.restassured;

import com.restassured.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestUserProfile extends Variables {

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

    //TC_Perfil_Usuario_0001
    @Tag("Smoke")
    @Test
    public void ViewUserProfileSuccess200() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("user"))
                    .body("user", hasKey("name"))
                    .body("user.name", Matchers.equalTo("user1"))
                    .body("user", hasKey("last_name"))
                    .body("user.last_name", Matchers.equalTo("user1"))
                    .body("user", hasKey("dni"))
                    .body("user.dni", Matchers.equalTo(987654321))
                    .body("user", hasKey("email"))
                    .body("user.email", Matchers.equalTo("user1@mail.com"))
                    .body("user", hasKey("user_id"))
                    .body("user.user_id", Matchers.equalTo(2))
                    .body("user", hasKey("phone"))
                    .body("user.phone", Matchers.equalTo(1226489722))
                    .body("user", not(hasKey("password")))
                    .body("user", hasKey("accountId"))
                    .body("account.account_id", Matchers.equalTo(2))
                    .body("account", hasKey("cvu"))
                    .body("account.cvu", Matchers.equalTo("1828142364587587493333"))
                    .body("account.cvu", Matchers.hasLength(22))
                    .body("account", hasKey("alias"))
                    .body("account.alias", Matchers.equalTo("afectacion.divisa.cambios"))
                    .body("account", hasKey("availableBalance"))
                    .body("account.availableBalance", Matchers.equalTo(220000f))
                    .body("user", not(hasKey("password")))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Perfil_Usuario_0002
    @Tag("Smoke")
    @Test
    public void ViewUserProfileFailure401() {

        Response response;

        response = given()
                    .basePath("/users/{id}")
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

    //TC_Perfil_Usuario_0003
    @Tag("Smoke")
    @Test
    public void ViewUserProfileFailure404() {

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 999).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.TEXT)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Perfil_Usuario_0004
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileSuccess200() {

        Response response;

        User user = new User();
        user.setName("Pablo");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(user).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$",hasKey("user_id"))
                    .body("$",hasKey("name"))
                    .body("$",hasKey("last_name"))
                    .body("$",hasKey("dni"))
                    .body("$",hasKey("email"))
                    .body("$",hasKey("phone"))
                    .body("$",hasKey("cvu"))
                    .body("$",hasKey("alias"))
                    .body("$",hasKey("accountId"))
                    .body("$", not(hasKey("password")))
                    .body("name", Matchers.equalTo("Pablo"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Perfil_Usuario_0005
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileFailure401() {

        Response response;

        User user = new User();
        user.setName("Pablo");

        response = given()
                    .basePath("/users/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(user).
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

    //TC_Perfil_Usuario_0006
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileFailure404() {

        Response response;

        User user = new User();
        user.setName("Carlos");

        response = given()
                .header("Authorization", "Bearer " + token)
                .basePath("/users/{id}")
                .pathParams("id", 99)
                .contentType(ContentType.JSON)
                .body(user).
                        when().
                patch().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.TEXT)
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Perfil_Usuario_0007
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileFailure400DniInUse() {

        Response response;

        User user = new User();
        user.setDni(123456789);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(user).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.TEXT)
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Perfil_Usuario_0008
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileFailure400EmailInUse() {

        Response response;

        User user = new User();
        user.setEmail("admin@mail.com");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 2)
                    .contentType(ContentType.JSON)
                    .body(user).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.TEXT)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Perfil_Usuario_0009
    @Tag("Smoke")
    @Test
    public void UpdateUserProfileFailure400SamePassword() {

        Response response;

        User user = new User();
        user.setPassword("123456789");

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/users/{id}")
                    .pathParams("id", 1)
                    .contentType(ContentType.JSON)
                    .body(user).
                when().
                    patch().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.TEXT)
                    .log().all()
                    .extract()
                    .response();

    }

}
