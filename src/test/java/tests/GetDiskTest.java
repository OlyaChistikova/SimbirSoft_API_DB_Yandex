package tests;

import helpers.BaseRequests;
import org.testng.annotations.Test;

import static helpers.BaseRequests.DISK_PATH;
import static helpers.BaseRequests.LOGIN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetDiskTest extends BaseTest{

    @Test
    public void getDiskWithAuthTest(){
        given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(200)
                .body("user.login", equalTo(LOGIN))
                .body("user.display_name", equalTo(LOGIN));
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
                .body("message", notNullValue());
    }
}