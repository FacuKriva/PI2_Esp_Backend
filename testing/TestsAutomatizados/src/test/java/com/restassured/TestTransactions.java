package com.restassured;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.collect.Ordering;
import com.restassured.model.Card;
import com.restassured.model.TransactionDeposit;
import com.restassured.reports.ExtentFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.testng.annotations.BeforeTest;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestTransactions extends Variables {

    private static String token;
    private static String token_id_4;

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
                    //account_id:2
                    .formParam("username", username)
                    .formParam("password", password)
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

    //**-------------------------- GET last 5 transactions (/accounts/{id}/transactions) ------------------------**

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
        test.info("Consulta exitosa de ultimas 5 transacciones de la cuenta. Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        List<String> transactionDateList = given()
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
                    .jsonPath().getList("transactions.realizationDate");


        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        for (String dateT:transactionDateList) {
            String date = dateT.replace("T", " ");
            LocalDateTime newDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTimeList.add(newDate);
        }

        LocalDateTime date1 = localDateTimeList.get(0);
        LocalDateTime date2 = localDateTimeList.get(1);
        Assertions.assertTrue(date1.isAfter(date2));
        Assertions.assertFalse(date1.isBefore(date2));
        Assertions.assertTrue(Ordering.natural().reverse().isOrdered(localDateTimeList));

        localDateTimeList.add(0, LocalDateTime.now());
        Assertions.assertTrue(Ordering.natural().reverse().isOrdered(localDateTimeList));

        localDateTimeList.add(2, LocalDateTime.now());
        Assertions.assertFalse(Ordering.natural().reverse().isOrdered(localDateTimeList));

}

    //TC_Transacciones_0002
    @Tag("Smoke")
    @Test
    @Order(2)
    public void ViewLastFiveTransactionsFailure404() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0002 - GET last 5 transactions by account id - Status Code: 404 - Not Found (Account ID)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 2");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. Usuario logueado. ID de cuenta inexistente.");

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
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
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
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .basePath("/accounts/{id}/transactions")
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
        test.info("Consulta Exitosa de ultimas 5 transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El usuario no posee ninguna transacción. El ID de cuenta corresponde al usuario.");

        Login_Id_4();

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token_id_4)
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

    //TC_Transacciones_0005
    @Tag("Smoke")
    @Test
    @Order(5)
    public void ViewLastFiveTransactionsFailure403() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0005 - GET last 5 transactions by account id - Status Code: 403 - Forbidden");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El ID de cuenta no corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions")
                    .pathParams("id", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("message", Matchers.equalTo("You don't have access to that account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //**---------------------------- GET all transactions (/accounts/{id}/activity) --------------------------**


    //TC_Transacciones_0006
    @Tag("Smoke")
    @Test
    @Order(6)
    public void ViewAllTransactionsSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0006 - GET all transactions by account id - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta. Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        List<String> transactionDateList = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity")
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
                    .body("transactions.size()", Matchers.greaterThanOrEqualTo(1))
                    .log().all()
                    .extract()
                    .jsonPath().getList("transactions.realizationDate");


        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        for (String dateT:transactionDateList) {
            String date = dateT.replace("T", " ");
            LocalDateTime newDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTimeList.add(newDate);
        }

        LocalDateTime date1 = localDateTimeList.get(0);
        LocalDateTime date2 = localDateTimeList.get(1);
        Assertions.assertTrue(date1.isAfter(date2));
        Assertions.assertFalse(date1.isBefore(date2));
        Assertions.assertTrue(Ordering.natural().reverse().isOrdered(localDateTimeList));

        localDateTimeList.add(0, LocalDateTime.now());
        Assertions.assertTrue(Ordering.natural().reverse().isOrdered(localDateTimeList));

        localDateTimeList.add(2, LocalDateTime.now());
        Assertions.assertFalse(Ordering.natural().reverse().isOrdered(localDateTimeList));

    }

    //TC_Transacciones_0007
    @Tag("Smoke")
    @Test
    @Order(7)
    public void ViewAllTransactionsFailure404() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0007 - GET al transactions by account id - Status Code: 404 - Not Found (Account ID)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de todas las transacciones de la cuenta. Usuario logueado. ID de cuenta inexistente");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity")
                    .pathParams("id", 99).
                when().
                    get().
                then()
                .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .contentType(ContentType.JSON)
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Transacciones_0008
    @Tag("Smoke")
    @Test
    @Order(8)
    public void ViewAllTransactionsFailure401() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0008 - GET all transactions by account id - Status Code: 401 - Unauthorized");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de todas las transacciones de la cuenta. Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .basePath("/accounts/{id}/activity")
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

    //TC_Transacciones_0009
    @Tag("Smoke")
    @Test
    @Order(9)
    public void ViewAllTransactionsSuccess204() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0009 - GET all transactions by account id - Status Code: 204 - No Content");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - No Content");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta Exitosa de todas las transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El ID de cuenta corresponde al usuario. El usuario no posee ninguna transacción");

        Login_Id_4();

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token_id_4)
                    .basePath("/accounts/{id}/activity")
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

    //TC_Transacciones_0010
    @Tag("Smoke")
    @Test
    @Order(10)
    public void ViewAllTransactionsFailure403() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0010 - GET all transactions by account id - Status Code: 403 - Forbidden");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de todas las transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El ID de cuenta no corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity")
                    .pathParams("id", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("message", Matchers.equalTo("You don't have access to that account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //**--------------- GET a transaction by id (/accounts/{id}/activity/activity/{transferencesId}) -------------**

    //TC_Transacciones_0011
    @Tag("Smoke")
    @Test
    @Order(11)
    public void ViewTransactionByIdSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0011 - GET a an account's transaction by id - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de una transacción en particular (por su id). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/{transferencesId}")
                    .pathParams("id", 2)
                    .pathParams("transferencesId", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(200)
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$", hasKey("amount"))
                    .body("$", hasKey("realizationDate"))
                    .body("$", hasKey("description"))
                    .body("$", hasKey("fromCvu"))
                    .body("$", hasKey("toCvu"))
                    .body("$", hasKey("type"))
                    .body("$", hasKey("transaction_id"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Transacciones_0012
    @Tag("Smoke")
    @Test
    @Order(12)
    public void ViewTransactionByIdFailure404AccountIdNotFound() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0012 - GET a an account's transaction by id - Status Code: 404 - Not Found (Account ID)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de una transacción en particular (por su id) asociada a un id de cuenta. Usuario logueado. ID de cuenta inexistente.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/{transferencesId}")
                    .pathParams("id", 99)
                    .pathParams("transferencesId", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Transacciones_0019
    @Tag("Smoke")
    @Test
    @Order(13)
    public void ViewTransactionByIdFailure404TransactionIdNotFound() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0012 - GET a an account's transaction by id - Status Code: 404 - Not Found (Transaction ID)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de una transacción en particular (por su id) asociada a un id de cuenta. Usuario logueado. ID de cuenta inexistente.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/{transferencesId}")
                    .pathParams("id", 2)
                    .pathParams("transferencesId", 99).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Transacciones_0013
    @Tag("Smoke")
    @Test
    @Order(14)
    public void ViewATransactionByIdFailure401() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0013 - GET a an account's transaction by id - Status Code: 401 - Unauthorized");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de una transacción en particular (por su id) asociada a un id de cuenta. Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .basePath("/accounts/{id}/activity/{transferencesId}")
                    .pathParams("id", 2)
                    .pathParams("transferencesId", 2).
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

    //TC_Transacciones_0014
    @Tag("Smoke")
    @Test
    @Order(15)
    public void ViewATransactionByIdFailure403() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0014 - GET a an account's transaction by id - Status Code: 403 - Forbidden");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de una transacción en particular (por su id) asociada a un id de cuenta. Usuario logueado. ID de cuenta existente. El ID de cuenta no corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/{transferencesId}")
                    .pathParams("id", 1)
                    .pathParams("transferencesId", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("message", Matchers.equalTo("You don't have access to that account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //**------------------ POST a transaction (add money from card) (/accounts/{id}/transferences) -----------------**


    //TC_Transactions_0015
    @Tag("Smoke")
    @Test
    @Order(16)
    public void AddTransactionDepositMoneySuccess201() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0015 - POST a transaction (deposit money) - Status Code: 201 - Created ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 201 - Created");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta exitosa de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta no asociada a otro id de cuenta.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(50.00, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(201)
                    .statusCode(HttpStatus.SC_CREATED)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$", hasKey("transactionId"))
                    .body("$", hasKey("amount"))
                    .body("amount", Matchers.equalTo(50.00F))
                    .body("$", hasKey("realizationDate"))
                    .body("$", hasKey("description"))
                    .body("description", Matchers.equalTo("You deposited $50.0 from Brubank S.A.U. Débito"))
                    .body("cardNumber", Matchers.equalTo("**** 6269"))
                    .body("$", hasKey("toCvu"))
                    .body("toCvu", Matchers.equalTo("1828142364587587493333"))
                    .body("$", hasKey("type"))
                    .body("type", Matchers.equalTo("INCOMING"))
                    .log().all()
                    .extract()
                    .response();
    }


//TC_Transactions_0016
        @Tag("Smoke")
        @Test
        @Order(17)
        public void AddTransactionDepositMoneyFailure400AmountZero() throws InterruptedException {

            test = extent.createTest("TC_Transactions_0016 - POST a transaction (deposit money) - Status Code: 400 - Bad Request ");
            test.assignCategory("Tarjetas");
            test.assignCategory("Suite: Smoke");
            test.assignCategory("Request Method: POST");
            test.assignCategory("Status Code: 400 - Bad Request");
            test.assignCategory("Sprint: 3");
            test.assignAuthor("Ana Laura Fidalgo");
            test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta no asociada a otro id de cuenta. El monto de la transacción es igual a 0 (cero).");

            Response response;

            TransactionDeposit transaction = new TransactionDeposit(0.00, 3L);

            response = given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-type", "application/json")
                        .contentType(ContentType.JSON)
                        .basePath("/accounts/{id}/transferences")
                        .pathParams("id", 2)
                        .body(transaction).
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
                        .body("message", Matchers.equalTo("The amount can't be 0. Please enter a valid amount"))
                        .log().all()
                        .extract()
                        .response();
        }

    //TC_Transactions_0017
    @Tag("Smoke")
    @Test
    @Order(18)
    public void AddTransactionDepositMoneyFailure400AmountNegative() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0017 - POST a transaction (deposit money) - Status Code: 400 - Bad Request ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta no asociada a otro id de cuenta. El monto de la transacción es un número negativo.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(-45.00, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
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
                    .body("message", Matchers.equalTo("The amount can't be negative. Please enter a valid amount"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0018
    @Tag("Smoke")
    @Test
    @Order(19)
    public void AddTransactionDepositMoneyFailure400CardExpired() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0018 - POST a transaction (deposit money) - Status Code: 400 - Bad Request ");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta no asociada a otro id de cuenta. La tarjeta está expirada.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 4L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
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
                    .body("message", Matchers.equalTo("The card you are trying to use is expired"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0020
    @Tag("Smoke")
    @Test
    @Order(20)
    public void AddTransactionDepositMoneyFailure401Unauthorized() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0020 - POST a transaction (deposit money) - Status Code: 401 - Unauthorized");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("401 - Unauthorized");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta no asociada a otro id de cuenta.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 3L);

        response = given()
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
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

    //TC_Transactions_0021
    @Tag("Smoke")
    @Test
    @Order(21)
    public void AddTransactionDepositMoneyFailure403ForbiddenAccountId() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0021 - POST a transaction (deposit money) - Status Code: 403 - Forbidden");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta no corresponde al usuario. Tarjeta no asociada a otro id de cuenta.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 1)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("message", Matchers.equalTo("You don't have access to that account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0022
    @Tag("Smoke")
    @Test
    @Order(22)
    public void AddTransactionDepositMoneyFailure403ForbiddenCardId() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0022 - POST a transaction (deposit money) - Status Code: 403 - Forbidden");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. Tarjeta asociada a otro id de cuenta.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 1L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("message", Matchers.equalTo("The card doesn't belong to the account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0023
    @Tag("Smoke")
    @Test
    @Order(23)
    public void AddTransactionDepositMoneyFailure404AccountId() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0023 - POST a transaction (deposit money) - Status Code: 404 - Not Found (Account Id)");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta inexistente. Tarjeta no asociada a otro id de cuenta.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 99)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0024
    @Tag("Smoke")
    @Test
    @Order(24)
    public void AddTransactionDepositMoneyFailure404CardId() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0024 - POST a transaction (deposit money) - Status Code: 404 - Not Found (Card Id)");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. ID de tarjeta inexistente.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.00, 99L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0025
    @Tag("Regression")
    @Test
    @Order(25)
    public void AddTransactionDepositMoneyFailure400AmountMoreThan2Decimals() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0025 - POST a transaction (deposit money) - Status Code: 400 - Bad Request (Amount has more than 2 decimals)");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. El monto de la transacción posee más de dos decimales.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.012, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Bad Request"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The amount must be a number with two decimal places"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transactions_0026
    @Tag("Regression")
    @Test
    @Order(26)
    public void AddTransactionDepositMoneyFailure400AmountMoreThan2DecimalsAllZero() throws InterruptedException {

        test = extent.createTest("TC_Transactions_0026 - POST a transaction (deposit money) - Status Code: 201 - Created (Amount has more than 2 decimals ALL zero)");
        test.assignCategory("Tarjetas");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: POST");
        test.assignCategory("201 - Created");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Alta fallida de nueva transacción (deposito de dinero a billetera desde tarjeta). Usuario logueado. ID de cuenta existente. Tarjeta no asociada a otro id de cuenta. El monto de la transacción posee más de dos decimales que son todos 0.");

        Response response;

        TransactionDeposit transaction = new TransactionDeposit(100.000, 3L);

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-type", "application/json")
                    .contentType(ContentType.JSON)
                    .basePath("/accounts/{id}/transferences")
                    .pathParams("id", 2)
                    .body(transaction).
                when().
                    post().
                then()
                    .assertThat()
                    .statusCode(201)
                    .statusCode(HttpStatus.SC_CREATED)
                    .contentType(ContentType.JSON)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$", hasKey("transactionId"))
                    .body("$", hasKey("amount"))
                    .body("amount", Matchers.equalTo(100.00F))
                    .body("$", hasKey("realizationDate"))
                    .body("$", hasKey("description"))
                    .body("description", Matchers.equalTo("You deposited $100.0 from Brubank S.A.U. Débito"))
                    .body("cardNumber", Matchers.equalTo("**** 6269"))
                    .body("$", hasKey("toCvu"))
                    .body("toCvu", Matchers.equalTo("1828142364587587493333"))
                    .body("$", hasKey("type"))
                    .body("type", Matchers.equalTo("INCOMING"))
                    .log().all()
                    .extract()
                    .response();
    }

    //**--------- GET all transactions filtered by amount (/accounts/{id}/activity/amount/{amountRange}) ---------**

    //TC_Transacciones_0027
    @Tag("Smoke")
    @Test
    @Order(27)
    public void ViewAllTransactionsFilteredByAmountRangeSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0027 - GET all transactions filtered by amount - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por rango de monto 3 (amount entre 5000 y 20000). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        List<Float> transactionAmountList = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/amount/{amountRange}")
                    .pathParams("id", 2)
                    .pathParams("amountRange", 3).
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
                    .body("transactions.size()", Matchers.greaterThanOrEqualTo(1))
                    .log().all()
                    .extract()
                    .jsonPath().getList("transactions.amount");

        List<Float> transactionAmountNewList = new ArrayList<>();
        for (Float amount:transactionAmountList) {
            if (amount>= 5000 && amount <= 20000){
                transactionAmountNewList.add(amount);
          }
        }
    Assertions.assertTrue(transactionAmountList.size() == transactionAmountNewList.size());

    }

    //TC_Transacciones_0028
    @Tag("Smoke")
    @Test
    @Order(28)
    public void ViewAllTransactionsFilteredByAmountRangeSuccess204() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0028 - GET all transactions filtered by amount - Status Code: 204 - No Content");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - No Content");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por rango de monto 2 (amount entre 1000 y 5000). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/amount/{amountRange}")
                    .pathParams("id", 2)
                    .pathParams("amountRange", 2).
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

    //TC_Transacciones_0029
    @Tag("Regression")
    @Test
    @Order(29)
    public void ViewAllTransactionsFilteredByAmountRangeFailure400RangeLessThan1() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0029 - GET all transactions filtered by amount - Status Code: 400 - Bad Request (Range < 1)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por rango de monto < 1 (los rangos posibles son entre 1 y 5). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/amount/{amountRange}")
                    .pathParams("id", 2)
                    .pathParams("amountRange", 0).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Select index out of range"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("Please select a option within the range"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transacciones_0030
    @Tag("Regression")
    @Test
    @Order(30)
    public void ViewAllTransactionsFilteredByAmountRangeFailure400RangeGreaterThan5() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0030 - GET all transactions filtered by amount - Status Code: 400 - Bad Request (Range > 5)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por rango de monto > 5 (los rangos posibles son entre 1 y 5). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/amount/{amountRange}")
                    .pathParams("id", 2)
                    .pathParams("amountRange", 6).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Select index out of range"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("Please select a option within the range"))
                    .log().all()
                    .extract()
                    .response();
    }

    //**----------------- GET all transactions filtered by type (/accounts/{id}/activity/type) ---------------**

    //TC_Transacciones_0031
    @Tag("Smoke")
    @Test
    @Order(31)
    public void ViewAllTransactionsFilteredByTypeIncomingSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0031 - GET all transactions filtered by type - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion (INCOMING). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        List<String> transactionTypeList = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 2)
                    .queryParam("transaction_type", "INCOMING").
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
                    .body("transactions.size()", Matchers.greaterThanOrEqualTo(1))
                    .body("transactions.type", Matchers.everyItem(equalTo("INCOMING")))
                    .log().all()
                    .extract()
                    .jsonPath().getList("transactions.type");

                    //System.out.println(transactionTypeList);
    }

    //TC_Transacciones_0032
    @Tag("Smoke")
    @Test
    @Order(32)
    public void ViewAllTransactionsFilteredByTypeOutgoingSuccess200() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0032 - GET all transactions filtered by type - Status Code: 200 - OK");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 200 - OK");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion (OUTGOING). Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        List<String> transactionTypeList = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 2)
                    .queryParam("transaction_type", "OUTGOING").
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
                    .body("transactions.size()", Matchers.greaterThanOrEqualTo(1))
                    .body("transactions.type", Matchers.everyItem(equalTo("OUTGOING")))
                    .log().all()
                    .extract()
                    .jsonPath().getList("transactions.type");

                //System.out.println(transactionTypeList);
    }

    //TC_Transacciones_0033
    @Tag("Smoke")
    @Test
    @Order(33)
    public void ViewAllTransactionsFilteredByTypeFailure400WrongTransactionType() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0033 - GET all transactions filtered by type - Status Code: 400 - Bad Request (Wrong transaction type)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 400 - Bad Request");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion. La transaccion no es de tipo OUTGOING ni INCOMING. Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 2)
                    .queryParam("transaction_type", "DEPOSIT").
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(400)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Bad Request"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("The transaction is not correct"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transacciones_0034
    @Tag("Smoke")
    @Test
    @Order(34)
    public void ViewAllTransactionsFilteredByTypeFailure404AccountID() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0034 - GET all transactions filtered by type - Status Code: 404 - Not Found (Account ID)");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion. Usuario logueado. ID de cuenta inexistente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 99)
                    .queryParam("transaction_type", "INCOMING").
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Not Found"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transacciones_0035
    @Tag("Smoke")
    @Test
    @Order(35)
    public void ViewAllTransactionsFilteredByTypeFailure401() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0035 - GET all transactions filtered by type - Status Code: 401 - Unauthorized");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 401 - Unauthorized");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion. Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 2)
                    .queryParam("transaction_type", "INCOMING").
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

    //TC_Transacciones_0036
    @Tag("Smoke")
    @Test
    @Order(36)
    public void ViewAllTransactionsFilteredByTypeFailure403() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0036 - GET all transactions filtered by type - Status Code: 403 - Forbidden");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion. Usuario logueado. ID de cuenta existente. El ID de cuenta no corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 1)
                    .queryParam("transaction_type", "INCOMING").
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .body("$", Matchers.instanceOf(Map.class))
                    .body("$",hasKey("error"))
                    .body("error", Matchers.equalTo("Forbidden"))
                    .body("$",hasKey("message"))
                    .body("message", Matchers.equalTo("You don't have access to that account"))
                    .log().all()
                    .extract()
                    .response();
    }

    //TC_Transacciones_0037
    @Tag("Smoke")
    @Test
    @Order(37)
    public void ViewAllTransactionsFilteredByTypeSuccess204() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0037 - GET all transactions filtered by type - Status Code: 204 - No Content");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 204 - No Content");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta exitosa de todas las transacciones de la cuenta (actividad) filtrada por tipo de transaccion. Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario. La cuenta no registra transacciones.");

        Response response;

        Login_Id_4();

        response = given()
                    .header("Authorization", "Bearer " + token_id_4)
                    .basePath("/accounts/{id}/activity/type")
                    .pathParams("id", 4)
                    .queryParam("transaction_type", "INCOMING").
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


    //**--------------------------------------------------- AUX -------------------------------------------------**

    public static void Login_Id_4() {
        token_id_4 = given()
                .auth().preemptive()
                .basic(client_id, client_secret)
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "password")
                //account_id: 4
                .formParam("username", "amaria@mail.com")
                .formParam("password", password)
                .basePath("/security/oauth/token")
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .jsonPath().get("access_token");
    }

}
