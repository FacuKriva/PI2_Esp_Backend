package com.restassured;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.restassured.reports.ExtentFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestTransactions extends Variables {

    private static String token;

    static ExtentSparkReporter spark = new ExtentSparkReporter("target/TransactionsTestsReport.html");
    static ExtentReports extent;
    ExtentTest test;

    @BeforeAll
    public static void Setup() {

        RestAssured.baseURI = base_uri;

        extent = ExtentFactory.getInstance();
        extent.attachReporter(spark);

    }

    @BeforeAll
    public static void Login() {
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

    //**---------------------------- GET last 5 transactions (/accounts/{id}/transactions) --------------------------**

    //TC_Transacciones_0001
    @Tag("Smoke")
    @Test
    @Order(1)
    public void ViewLastFiveTransactionsSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0001 - GET last 5 transactions by account id - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de ultimas 5 transacciones de la cuenta. Usuario logueado. ID de cuenta existente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions")
                    .pathParams("id", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$", hasKey("account"))
                    .body("$", hasKey("transactions"))
                    .body("transactions[0]", hasKey("amount"))
                    .body("transactions[0]", hasKey("realizationDate"))
                    .body("transactions[0]", hasKey("description"))
                    .body("transactions[0]", hasKey("fromCvu"))
                    .body("transactions[0]", hasKey("toCvu"))
                    .body("transactions[0]", hasKey("type"))
                    .body("transactions[0]", hasKey("transaction_id"))
                    .body("transactions.size()", Matchers.lessThanOrEqualTo(5))
                    .body("transactions.size()", Matchers.greaterThanOrEqualTo(1))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Transacciones_0002
    @Tag("Smoke")
    @Test
    @Order(2)
    public void ViewLastFiveTransactionsFailure404() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0002 - GET last 5 transactions by account id - Status Code: 404 - Not Found");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. Usuario logueado. ID de cuenta inexistente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions")
                    .pathParams("id", 99).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The account doesn't exist"))
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Transacciones_0003
    @Tag("Smoke")
    @Test
    @Order(3)
    public void ViewLastFiveTransactionsFailure401() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0003 - GET last 5 transactions by account id - Status Code: 401 - Unauthorized");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. Usuario no logueado. ID de cuenta existente");

        Response response;

        response = given()
                    .basePath("/accounts/{id}/transactions")
                    .pathParams("id", 4).
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

    //TC_Transacciones_0004
    @Tag("Smoke")
    @Test
    @Order(4)
    public void ViewLastFiveTransactionsSuccess204() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0004 - GET last 5 transactions by account id - Status Code: 204 - No Content");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - No Content");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta Exitosa de ultimas 5 transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El usuario no posee ninguna transacción");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions")
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

}
