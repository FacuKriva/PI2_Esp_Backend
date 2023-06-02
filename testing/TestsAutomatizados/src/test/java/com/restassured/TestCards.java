package com.restassured;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.restassured.model.Card;
import com.restassured.reports.ExtentFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCards extends Variables {

    private static String token;

    static ExtentSparkReporter spark = new ExtentSparkReporter("target/CardsTestsReport.html");
    static ExtentReports extent;
    ExtentTest test;


    @BeforeAll
    public static void  Setup() {

        RestAssured.baseURI = base_uri;

        extent = ExtentFactory.getInstance();
        extent.attachReporter(spark);

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


    @AfterAll
    public static void quit() {
        extent.flush();
    }

    //**---------------------------------- GET all cards (/accounts/{id}/cards) ----------------------------------**

    //TC_Tarjetas_0001
    @Tag("Smoke")
    @Test
    @Order(1)
    public void ViewAllCardsSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0001 - GET all cards by account id - Status Code: 200 - OK ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización exitosa de listas de tarjetas asociadas a la cuenta del usuario. Usuario logueado. ID de cuenta existente");

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

    //TC_Tarjetas_0002
    @Tag("Smoke")
    @Test
    @Order(2)
    public void ViewAllCardsSuccess204() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0002 - GET all cards by account id - Status Code: 204 - No Content ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - No Content");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización exitosa de listas de tarjetas asociadas a la cuenta del usuario. Usuario sin tarjetas asociadas. Usuario logueado. ID de cuenta existente");


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
    @Order(3)
    public void ViewAllCardsFailure401() throws InterruptedException{

        test = extent.createTest("TC_Tarjetas_0003 - GET all cards by account id - Status Code: 401 - Unauthorized ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización fallida de listas de tarjetas asociadas a la cuenta del usuario. Usuario no logueado. ID de cuenta existente");

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
    @Order(4)
    public void ViewAllCardsFailure404() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0004 - GET all cards by account id - Status Code: 404 - Not Found ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización fallida de listas de tarjetas asociadas a la cuenta del usuario. Usuario logueado. ID de cuenta inexistente");

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

    //**------------------------------- GET a card by card id (/accounts/{id}/cards) ------------------------------**

    //TC_Tarjetas_0005
    @Tag("Smoke")
    @Test
    @Order(5)
    public void ViewACardSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0005 - GET a card by id - Status Code: 200 - OK ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización exitosa de los datos de una tarjeta en particular asociada a la cuenta de un usuario. Usuario logueado. ID de cuenta existente. ID de tarjeta existente");

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
    @Order(6)
    public void ViewACardFailure401() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0006 - GET a card by id - Status Code: 401 - Unauthorized ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización fallida de los datos de una tarjeta en particular asociada a la cuenta de un usuario.Usuario no logueado. ID de cuenta existente. ID de tarjeta existente");


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
    @Order(7)
    public void ViewACardFailure404AccountNotFound() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0008 - GET a card by id - Status Code: 404 - Not Found ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización fallida de los datos de una tarjeta en particular asociada a la cuenta de un usuario.Usuario logueado. ID de cuenta inexistente. ID de tarjeta existente");

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
    @Order(8)
    public void ViewACardFailure404CardNotFound() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0009 - GET a card by id - Status Code: 404 - Not Found ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Visualización fallida de los datos de una tarjeta en particular asociada a la cuenta de un usuario.Usuario logueado. ID de cuenta existente. ID de tarjeta inexistente");

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

    //**------------------------------------ POST a card (/accounts/{id}/cards)-----------------------------------**

    //TC_Tarjetas_0010
    @Tag("Smoke")
    @Test
    @Order(9)
    public void AddCardSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0010 - POST a card to an account - Status Code: 200 - OK ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta exitosa de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente");

        Response response;

        Card card = new Card("DH Money", 4817475789962098L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

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
                    .body("cardNumber", Matchers.equalTo(4817475789962098L))
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

    //TC_Tarjetas_0011
    @Tag("Smoke")
    @Test
    @Order(10)
    public void AddCardFailure409() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0011 - POST a card to an account - Status Code: 409 - Conflict");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 409 - Conflict");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta ya asociada a otro id de cuenta");

        Response response;

        Card card = new Card("DH Money", 5412873403403000L, "Martina Zeta", "08/2026", 827, "Digital Money Bank", "Debito");

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

    //TC_Tarjetas_0012
    @Tag("Smoke")
    @Test
    @Order(11)
    public void AddCardFailure404() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0012 - POST a card to an account - Status Code: 404 - Not Found");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta inexistente. Tarjeta no asociada a otro id de cuenta");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Martina Zeta", "08/2026", 827, "Digital Money Bank", "Debito");

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


    //TC_Tarjetas_0013
    @Tag("Smoke")
    @Test
    @Order(12)
    public void AddCardFailure401() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0013 - POST a card to an account - Status Code: 401 - Unauthorized");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario no logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

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


    //TC_Tarjetas_0022
    @Tag("Smoke")
    @Test
    @Order(13)
    public void AddCardFailure400ExpirationDateFormat() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0022 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Formato incorrecto (expirationDate)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Martina Zeta", "08/01/2026", 827, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Bad Request"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The expirationDate must be in the format MM/yyyy"))
                    .log().all()
                    .extract()
                    .response();
    }


    //TC_Tarjetas_0014
    @Tag("Smoke")
    @Test
    @Order(14)
    public void AddCardFailure400CardHolderEmpty() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0014 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (cardHolder)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "", "08/2026", 827, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0015
    @Tag("Smoke")
    @Test
    @Order(15)
    public void AddCardFailure400CardNumberZero() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0015 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (cardNumber = 0)");

        Response response;

        Card card = new Card("DH Money", 0L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0016
    @Tag("Smoke")
    @Test
    @Order(16)
    public void AddCardFailure400CardExpirationDateEmpty() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0016 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (expirationDate)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Juan Pedro Perez", "", 827, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0017
    @Tag("Smoke")
    @Test
    @Order(17)
    public void AddCardFailure400CardTypeEmpty() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0017 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (cardType)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Tarjetas_0018
    @Tag("Smoke")
    @Test
    @Order(18)
    public void AddCardFailure400CardCvvZero() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0018 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (cvv = 0)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Juan Pedro Perez", "08/2026", 0, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The cvv must be between 3 and 4 digits"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0019
    @Tag("Smoke")
    @Test
    @Order(19)
    public void AddCardFailure400CardAliasEmpty() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0019 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (alias)");

        Response response;

        Card card = new Card("", 4539337010734513L, "Juan Pedro Perez", "08/2026", 827, "Digital Money Bank", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Tarjetas_0020
    @Tag("Smoke")
    @Test
    @Order(20)
    public void AddCardFailure400CardBankEmpty() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0020 - POST a card to an account - Status Code: 400 - Bad Request");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. Dato vacío (bank)");

        Response response;

        Card card = new Card("DH Money", 4539337010734513L, "Juan Pedro Perez", "08/2026", 827, "", "Debito");

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
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }


//**--------------------------------- DELETE a card (/accounts/{id}/cards/{idCard})-------------------------------**

    //TC_Tarjetas_0028
    @Tag("Smoke")
    @Test
    @Order(21)
    public void DeleteCardSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0028 - DELETE a card by id - Status Code: 200 - OK\n");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: DELETE");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Baja exitosa de tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. ID de tarjeta existente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 5).
                when().
                    delete().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Tarjetas_0029
    @Tag("Smoke")
    @Test
    @Order(22)
    public void DeleteCardFailure401() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0029 - DELETE a card by id - Status Code: 401 - Unauthorized");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: DELETE");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Baja fallida de tarjeta asociada a cuenta del usuario. Usuario no logueado. ID de cuenta existente. ID de tarjeta existente");

        Response response;

        response = given()
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 2).
                when().
                    delete().
                then()
                    .assertThat()
                    .statusCode(401)
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .log().all()
                    .extract()
                    .response();
    }



    //TC_Tarjetas_0030
    @Tag("Smoke")
    @Test
    @Order(23)
    public void DeleteCardFailure404CardNotFound() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0030- DELETE a card by id - Status Code: 404 - Not Found");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: DELETE");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Baja fallida de tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta existente. ID de tarjeta inexistente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 1)
                    .pathParams("idCard", 99).
                when().
                    delete().
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

    //TC_Tarjetas_0031
    @Tag("Smoke")
    @Test
    @Order(24)
    public void DeleteCardFailure404AccountNotFound() throws InterruptedException {

        test = extent.createTest("TC_Tarjetas_0031- DELETE a card by id - Status Code: 404 - Not Found");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: DELETE");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Baja fallida de tarjeta asociada a cuenta del usuario. Usuario logueado. ID de cuenta inexistente. ID de tarjeta existente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/cards/{idCard}")
                    .pathParams("id", 99)
                    .pathParams("idCard", 2).
                when().
                    delete().
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


}
