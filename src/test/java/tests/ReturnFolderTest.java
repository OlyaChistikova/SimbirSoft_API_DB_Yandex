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
        createResource(RESOURCES_PATH, Map.of("path", folderName), 201);
        deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);
    }

    @AfterClass
    public void cleanTrash(){
        deleteResource(TRASH_RESOURCES_PATH, null, 202);
    }

    @Test
    public void returnRemovedFolderTest(){
        Response response = getResource(TRASH_RESOURCES_PATH, null, 200);
        List<Map<String, Object>> responseTrashList = response.jsonPath().getList("_embedded.items");

        boolean folderExists = responseTrashList.stream()
                .anyMatch(item -> folderName.equals(item.get("name")));
        Assert.assertTrue(folderExists);

        String folderPath = responseTrashList.stream()
                .filter(item -> folderName.equals(item.get("name")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Папка с именем " + folderName + " не найдена в корзине"))
                .get("path").toString();

        Response responseReturnFolder = createResource(RETURN_RESOURCES_PATH, Map.of("path", folderPath), 201);
        String encodedParam = URLEncoder.encode(folderName, StandardCharsets.UTF_8);

        Assert.assertEquals(responseReturnFolder.path("method"), "GET");
        Assert.assertTrue(responseReturnFolder.path("href").toString().endsWith(encodedParam));
        Assert.assertNotNull(responseReturnFolder.path("templated"));

        Response responseGetFolder = getResource(RESOURCES_PATH, Map.of("path", folderName), 200);

        Assert.assertEquals(responseGetFolder.path("name"), folderName);
        Assert.assertEquals(responseGetFolder.path("path"), "disk:/" + folderName);

        deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);
    }

    @Test
    public void returnRemovedFolderWithoutAuthTest(){
        Response response = getResource(TRASH_RESOURCES_PATH, null, 200);
        List<Map<String, Object>> responseTrashList = response.jsonPath().getList("_embedded.items");

        boolean folderExists = responseTrashList.stream()
                .anyMatch(item -> folderName.equals(item.get("name")));
        Assert.assertTrue(folderExists);

        String folderPath = responseTrashList.stream()
                .filter(item -> folderName.equals(item.get("name")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Папка с именем " + folderName + " не найдена в корзине"))
                .get("path").toString();

        Response responseReturnFolder = sendPutRequestWithoutAuth(RETURN_RESOURCES_PATH, Map.of("path", folderPath), 401);

        Assert.assertNotNull(responseReturnFolder.path("error"));
        Assert.assertNotNull(responseReturnFolder.path("description"));
        Assert.assertNotNull(responseReturnFolder.path("message"));

        Response responseGetFolder = getResource(RESOURCES_PATH, Map.of("path", folderName), 404);

        Assert.assertNotNull(responseGetFolder.path("error"));
        Assert.assertNotNull(responseGetFolder.path("description"));
        Assert.assertNotNull(responseGetFolder.path("message"));
    }

    /**
     * Отправляет PUT-запрос без авторизации и проверяет статус.
     */
    public Response sendPutRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendPutRequest(false, endpoint, params, expectedStatus);
    }
}