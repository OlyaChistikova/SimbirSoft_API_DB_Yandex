package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CreateFolderTest extends BaseTest {

    @AfterClass
    public void cleanTrash(){
        deleteResource(TRASH_RESOURCES_PATH, null, 202);
    }

    @DataProvider(name = "createSuccessNameFolder")
    public static Object[][] createNameFolder() {
        return new Object[][]{
                {"Моя папка"},
                {"П"},
                {"123"},
                {"%"}
        };
    }

    @Test(dataProvider = "createSuccessNameFolder")
    public void createFolderAuthTest(String folderName){
        String encodedParam = URLEncoder.encode(folderName, StandardCharsets.UTF_8);
        Response responseCreateFolder = createResource(RESOURCES_PATH, Map.of("path", folderName), 201);

        Assert.assertEquals(responseCreateFolder.path("method"), "GET");
        Assert.assertTrue(responseCreateFolder.path("href").toString().endsWith(encodedParam));
        Assert.assertNotNull(responseCreateFolder.path("templated"));

        Response responseGetFolder = getResource(RESOURCES_PATH, Map.of("path", folderName), 200);

        Assert.assertEquals(responseGetFolder.path("name"), folderName);
        Assert.assertEquals(responseGetFolder.path("path"), "disk:/" + folderName);

        deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);
    }

    @DataProvider(name = "createInvalidNameFolder")
    public static Object[][] createInvalidNameFolder() {
        return new Object[][]{
                {"//"},
                {"."}
        };
    }

    @Test(dataProvider = "createInvalidNameFolder")
    public void createFailedFolderWithInvalidParamsTest(String invalid_folder_name){
        Response responseCreateFolder = createResource(RESOURCES_PATH, Map.of("path", invalid_folder_name), 404);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        Response response = getResource(RESOURCES_PATH, Map.of("path", "disk:/"), 200);
        List<String> folderNames = response.jsonPath().getList("_embedded.items.name");

        Assert.assertFalse(folderNames.contains(invalid_folder_name));
    }

    @DataProvider(name = "createIncorrectNameFolder")
    public static Object[][] createIncorrectNameFolder() {
        return new Object[][]{
                {""},
                {":"}
        };
    }

    @Test(dataProvider = "createIncorrectNameFolder")
    public void createFailedFolderWithEmptyOrIncorrectParamsTest(String incorrect_folder_name){
        Response responseCreateFolder = createResource(RESOURCES_PATH, Map.of("path", incorrect_folder_name), 400);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        Response response = getResource(RESOURCES_PATH, Map.of("path", "disk:/"), 200);
        List<String> folderNames = response.jsonPath().getList("_embedded.items.name");

        Assert.assertFalse(folderNames.contains(incorrect_folder_name));
    }

    @Test
    public void createFolderWithoutAuthTest(){
        String folder_name = "Новая папка";
        Response responseCreateFolder = BaseRequests.sendPutRequestWithoutAuth(RESOURCES_PATH, Map.of("path", folder_name), 401);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        Response response = getResource(RESOURCES_PATH, Map.of("path", "disk:/"), 200);
        List<String> folderNames = response.jsonPath().getList("_embedded.items.name");

        Assert.assertFalse(folderNames.contains(folder_name));
    }
}