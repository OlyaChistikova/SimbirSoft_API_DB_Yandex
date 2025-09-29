package tests;

import helpers.BaseRequests;
import helpers.ParametersProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetDiskTest {

    /**
     * Токен авторизации для доступа к API.
     */
    private static final String TOKEN = ParametersProvider.getProperty("token");

    /**
     * Путь для взаимодействия с диском.
     */
    private static final String DISK_PATH = ParametersProvider.getProperty("disk_path");

    /**
     * Данные для авторизации
     */
    private static final String LOGIN = ParametersProvider.getProperty("username");

    @Test
    public void getDiskWithAuthTest(){
        given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(200)
                .body("user.login", equalTo(LOGIN))
                .body("user.display_name", equalTo(LOGIN))
                .log().all();
    }

    @Test
    public void getDiskWithoutAuthTest(){
        given()
                .spec(BaseRequests.requestSpec())
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(401)
                .body("error", equalTo("UnauthorizedError"))
                .body("description", notNullValue())
                .body("message", notNullValue())
                .log().all();
    }
}