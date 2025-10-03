package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetDiskTest extends BaseTest{

    @Test
    public void getDiskWithAuthTest(){
        Response response = given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.path("user.login"), LOGIN);
        Assert.assertEquals(response.path("user.display_name"), LOGIN);
    }

    @Test
    public void getDiskWithoutAuthTest(){
        Response response = given()
                .spec(BaseRequests.requestSpec())
                .when()
                .get(DISK_PATH)
                .then()
                .statusCode(401)
                .extract().response();
        Assert.assertEquals(response.path("error"), "UnauthorizedError");
        Assert.assertNotNull(response.path("description"));
        Assert.assertNotNull(response.path("message"));
    }
}