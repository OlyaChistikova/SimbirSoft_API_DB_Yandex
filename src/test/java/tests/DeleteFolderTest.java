package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static helpers.BaseRequests.*;

public class DeleteFolderTest extends BaseTest{
    private String folderName;

    @BeforeMethod
    public void createPostForDelete() {
        folderName = "Удаляемая папка";
        createResource(RESOURCES_PATH, Map.of("path", folderName), 201);
    }

    @AfterClass
    public void cleanTrash(){
        deleteResource(TRASH_RESOURCES_PATH, null, 202);
    }

    @Test
    public void deleteCorrectFolderWithAuthTest() {
        Response responseDeleteFolder = deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);

        Assert.assertTrue(responseDeleteFolder.getBody().asString().isEmpty());

        Response response = getResource(RESOURCES_PATH, Map.of("path", "disk:/"), 200);
        List<String> folderNames = response.jsonPath().getList("_embedded.items.name");

        Assert.assertFalse(folderNames.contains(folderName));
    }

    @Test
    public void deleteRemovedFolderWithAuthTest() {
        deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);

        Response responseGetFolder = getResource(RESOURCES_PATH, Map.of("path", folderName), 404);

        Assert.assertNotNull(responseGetFolder.path("error"));
        Assert.assertNotNull(responseGetFolder.path("description"));
        Assert.assertNotNull(responseGetFolder.path("message"));

        Response responseDeleteFolder = deleteResource(RESOURCES_PATH, Map.of("path", folderName), 404);

        Assert.assertNotNull(responseDeleteFolder.path("error"));
        Assert.assertNotNull(responseDeleteFolder.path("description"));
        Assert.assertNotNull(responseDeleteFolder.path("message"));
    }

    @Test
    public void deleteFolderWithoutAuthTest() {
        Response responseDeleteFolder = sendDeleteRequestWithoutAuth(RESOURCES_PATH, Map.of("path", folderName), 401);

        Assert.assertNotNull(responseDeleteFolder.path("error"));
        Assert.assertNotNull(responseDeleteFolder.path("description"));
        Assert.assertNotNull(responseDeleteFolder.path("message"));

        Response response = getResource(RESOURCES_PATH, Map.of("path", "disk:/"), 200);
        List<String> folderNames = response.jsonPath().getList("_embedded.items.name");

        Assert.assertTrue(folderNames.contains(folderName));

        deleteResource(RESOURCES_PATH, Map.of("path", folderName), 204);
    }

    /**
     * Отправляет DELETE-запрос без авторизации и проверяет статус.
     */
    public Response sendDeleteRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendDeleteRequest(false, endpoint, params, expectedStatus);
    }
}