package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static helpers.BaseRequests.*;

public class ReturnFolderTest extends BaseTest{
    private String folderName;

    @BeforeMethod
    public void deleteFolderForReturn(){
        folderName = "Возвращаемая папка";
        createFolderRequest(folderName);
        deleteFolderRequest(folderName);
    }

    @AfterClass
    public void cleanTrash(){
        cleanTrashResourcesAfterDelete();
    }

    @Test
    public void returnRemovedFolderTest(){
        List<Map<String, Object>> responseTrashList = checkTrashFoldersAuthRequest();

        boolean folderExists = responseTrashList.stream()
                .anyMatch(item -> folderName.equals(item.get("name")));
        Assert.assertTrue(folderExists);

        String folderPath = responseTrashList.stream()
                .filter(item -> folderName.equals(item.get("name")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Папка с именем " + folderName + " не найдена в корзине"))
                .get("path").toString();

        Response responseReturnFolder = returnFolderAuthRequest(folderPath);
        String encodedParam = URLEncoder.encode(folderName, StandardCharsets.UTF_8);

        Assert.assertEquals(responseReturnFolder.path("method"), "GET");
        Assert.assertTrue(responseReturnFolder.path("href").toString().endsWith(encodedParam));
        Assert.assertNotNull(responseReturnFolder.path("templated"));

        Response responseGetFolder = getFolderAuthRequest(folderName);

        Assert.assertEquals(responseGetFolder.path("name"), folderName);
        Assert.assertEquals(responseGetFolder.path("path"), "disk:/" + folderName);

        deleteFolderRequest(folderName);
    }

    @Test
    public void returnRemovedFolderWithoutAuthTest(){
        List<Map<String, Object>> responseTrashList = checkTrashFoldersAuthRequest();

        boolean folderExists = responseTrashList.stream()
                .anyMatch(item -> folderName.equals(item.get("name")));
        Assert.assertTrue(folderExists);

        String folderPath = responseTrashList.stream()
                .filter(item -> folderName.equals(item.get("name")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Папка с именем " + folderName + " не найдена в корзине"))
                .get("path").toString();

        Response responseReturnFolder = returnFolderWithoutAuthRequest(folderPath);

        Assert.assertNotNull(responseReturnFolder.path("error"));
        Assert.assertNotNull(responseReturnFolder.path("description"));
        Assert.assertNotNull(responseReturnFolder.path("message"));

        Response responseGetFolder = getUnExistentFolderAuthRequest(folderName);

        Assert.assertNotNull(responseGetFolder.path("error"));
        Assert.assertNotNull(responseGetFolder.path("description"));
        Assert.assertNotNull(responseGetFolder.path("message"));
    }
}