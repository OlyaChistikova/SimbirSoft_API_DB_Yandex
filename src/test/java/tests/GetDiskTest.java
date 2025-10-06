package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static helpers.BaseRequests.*;

public class GetDiskTest extends BaseTest{

    @Test
    public void getDiskWithAuthTest(){
        Response responseGetDisk = getResource(DISK_PATH, null, 200);

        Assert.assertEquals(responseGetDisk.path("user.login"), LOGIN);
        Assert.assertEquals(responseGetDisk.path("user.display_name"), LOGIN);
    }

    @Test
    public void getDiskWithoutAuthTest(){
        Response responseGetDisk = sendGetRequestWithoutAuth(DISK_PATH, null, 401);

        Assert.assertEquals(responseGetDisk.path("error"), "UnauthorizedError");
        Assert.assertNotNull(responseGetDisk.path("description"));
        Assert.assertNotNull(responseGetDisk.path("message"));
    }

    /**
     * Отправляет GET-запрос без авторизации и проверяет статус.
     */
    public Response sendGetRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendGetRequest(false, endpoint, params, expectedStatus);
    }
}