package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static helpers.BaseRequests.DISK_PATH;
import static helpers.BaseRequests.LOGIN;

public class GetDiskTest extends BaseTest{

    @Test
    public void getDiskWithAuthTest(){
        Response responseGetDisk = getResource(DISK_PATH, null, 200);

        Assert.assertEquals(responseGetDisk.path("user.login"), LOGIN);
        Assert.assertEquals(responseGetDisk.path("user.display_name"), LOGIN);
    }

    @Test
    public void getDiskWithoutAuthTest(){
        Response responseGetDisk = BaseRequests.sendGetRequestWithoutAuth(DISK_PATH, null, 401);

        Assert.assertEquals(responseGetDisk.path("error"), "UnauthorizedError");
        Assert.assertNotNull(responseGetDisk.path("description"));
        Assert.assertNotNull(responseGetDisk.path("message"));
    }
}