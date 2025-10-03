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

import static helpers.BaseRequests.getFolderAuthRequest;

public class CreateFolderTest extends BaseTest {

    @AfterClass
    public void cleanTrash(){
        cleanTrashResourcesAfterDelete();
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
        Response responseCreateFolder = BaseRequests.addFolderAuthRequest(folderName);

        Assert.assertEquals(responseCreateFolder.path("method"), "GET");
        Assert.assertTrue(responseCreateFolder.path("href").toString().endsWith(encodedParam));
        Assert.assertNotNull(responseCreateFolder.path("templated"));

        Response responseGetFolder = getFolderAuthRequest(folderName);

        Assert.assertEquals(responseGetFolder.path("name"), folderName);
        Assert.assertEquals(responseGetFolder.path("path"), "disk:/" + folderName);

        deleteFolderRequest(folderName);
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
        Response responseCreateFolder = BaseRequests.addFolderInvalidParamsRequest(invalid_folder_name);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        List<String> listFolders = BaseRequests.getAllNamesFoldersRequest();

        Assert.assertFalse(listFolders.contains(invalid_folder_name));
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
        Response responseCreateFolder = BaseRequests.addFolderIncorrectParamsRequest(incorrect_folder_name);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        List<String> listFolders = BaseRequests.getAllNamesFoldersRequest();

        Assert.assertFalse(listFolders.contains(incorrect_folder_name));
    }

    @Test
    public void createFolderWithoutAuthTest(){
        String folder_name = "Новая папка";
        Response responseCreateFolder = BaseRequests.addFolderWithoutAuthRequest(folder_name);

        Assert.assertNotNull(responseCreateFolder.path("error"));
        Assert.assertNotNull(responseCreateFolder.path("description"));
        Assert.assertNotNull(responseCreateFolder.path("message"));

        List<String> listFolders = BaseRequests.getAllNamesFoldersRequest();

        Assert.assertFalse(listFolders.contains(folder_name));
    }
}