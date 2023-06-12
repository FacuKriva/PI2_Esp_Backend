package com.restassured;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.collect.Ordering;
import com.restassured.reports.ExtentFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.testng.annotations.BeforeTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

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

        test = extent.createTest("TC_Transacciones_0002 - GET last 5 transactions by account id - Status Code: 404 - Not Found");
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
        test.info("Consulta fallida de ultimas 5 transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El usuario intenta acceder a las transacciones asociadas a un ID de cuenta que no es propio.");

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

    //TC_Transacciones_0007
    @Tag("Smoke")
    @Test
    @Order(7)
    public void ViewAllTransactionsFailure404() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0007 - GET al transactions by account id - Status Code: 404 - Not Found");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 404 - Not Found");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de las transacciones de la cuenta. Usuario logueado. ID de cuenta inexistente");

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
        test.info("Consulta fallida de las transacciones de la cuenta. Usuario no logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

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
        test.info("Consulta Exitosa de las transacciones de la cuenta. ID de cuenta existente. Usuario logueado.El ID de cuenta corresponde al usuario. El usuario no posee ninguna transacción");

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
        test.info("Consulta fallida de las transacciones de la cuenta. ID de cuenta existente. Usuario logueado. El usuario intenta acceder a las transacciones asociadas a un ID de cuenta que no es propio.");

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
        test.info("Consulta exitosa de una transacción en particular (por su id) asociada a un id de cuenta. Usuario logueado. ID de cuenta existente. El ID de cuenta corresponde al usuario.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions/{transferencesId}")
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
//                    .body("$", hasKey("amount"))
//                    .body("amount", Matchers.equalTo(100000))
//                    .body("$", hasKey("realizationDate"))
//                    .body("realizationDate", Matchers.equalTo("2023-05-10 08:30:20.000000"))
//                    .body("$", hasKey("description"))
//                    .body("description", Matchers.equalTo("received transaction"))
//                    .body("$", hasKey("fromCvu"))
//                    .body("fromCvu", Matchers.equalTo("1828142364587587491111"))
//                    .body("$", hasKey("toCvu"))
//                    .body("$", Matchers.equalTo("1828142364587587493333"))
//                    .body("$", hasKey("type"))
//                    .body("type", Matchers.equalTo("INCOMING"))
//                    .body("$", hasKey("transaction_id"))
//                    .body("transaction_id", Matchers.equalTo(2))
                    .log().all()
                    .extract()
                    .response();

    }

    //TC_Transacciones_0012
    @Tag("Smoke")
    @Test
    @Order(12)
    public void ViewTransactionByIdFailure404() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0012 - GET a an account's transaction by id - Status Code: 404 - Not Found");
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
                    .basePath("/accounts/{id}/transactions/{transferencesId}")
                    .pathParams("id", 99)
                    .pathParams("transferencesId", 2).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(404)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .log().all()
                    .extract()
                    .response();

    }


    //TC_Transacciones_0013
    @Tag("Smoke")
    @Test
    @Order(13)
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
                    .basePath("/accounts/{id}/transactions/{transferencesId}")
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
    @Order(14)
    public void ViewATransactionByIdFailure403() throws InterruptedException {

        test = extent.createTest("TC_Transacciones_0014 - GET a an account's transaction by id - Status Code: 403 - Forbidden");
        test.assignCategory("Transacciones");
        test.assignCategory("Suite: Smoke");
        test.assignCategory("Request Method: GET");
        test.assignCategory("Status Code: 403 - Forbidden");
        test.assignCategory("Sprint: 3");
        test.assignAuthor("Ana Laura Fidalgo");
        test.info("Consulta fallida de una transacción en particular (por su id) asociada a un id de cuenta. Usuario logueado. ID de cuenta existente. El usuario intenta acceder a las transacciones asociadas a un ID de cuenta que no es propio.");

        Response response;

        response = given()
                    .header("Authorization", "Bearer " + token)
                    .basePath("/accounts/{id}/transactions/{transferencesId}")
                    .pathParams("id", 1)
                    .pathParams("transferencesId", 1).
                when().
                    get().
                then()
                    .assertThat()
                    .statusCode(403)
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .log().all()
                    .extract()
                    .response();
    }


    //**----------------------------------------------------- AUX ------------------------------------------------**

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
