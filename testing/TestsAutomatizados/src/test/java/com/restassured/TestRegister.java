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

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

public class TestRegister extends Variables {

    @BeforeAll
    public static void  Setup() {
        RestAssured.baseURI = base_uri;
    }

    @Tag("Smoke")
    @Test
    public void RegisterSuccess201() {

        Response response;

//        JSONObject request = new JSONObject();
//        request.put("name", "Ana");
//        request.put("last_name", "Fidalgo");
//        request.put("dni", 30744221);
//        request.put("email", "analaurafidalgo@gmail.com");
//        request.put("password", "123456789");
//        request.put("phone", 47828304);

        User user = new User("Ana", "Fidalgo", 30744221, "analaurafidalgo@gmail.com", "123456789", 47828304);

        response = given()
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/users")
                    //.body(request.toJSONString()).
                    .body(user).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(201)
                    .statusCode(HttpStatus.SC_CREATED)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("name"))
                    .body("name", Matchers.equalTo("Ana"))
                    .body("$",hasKey("last_name"))
                    .body("last_name", Matchers.equalTo("Fidalgo"))
                    .body("$",hasKey("dni"))
                    .body("dni", Matchers.equalTo(30744221))
                    .body("$",hasKey("email"))
                    .body("email", Matchers.equalTo("analaurafidalgo@gmail.com"))
                    .body("phone", Matchers.equalTo(47828304))
                    .body("$",hasKey("cvu"))
                    .body("$",hasKey("alias"))
                    .body("$",hasKey("user_id"))
                    .body("$", not(hasKey("password")))
                    .log().all()
                    .extract()
                    .response();

    }


}
