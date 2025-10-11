package tests;

import helpers.JsonUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo.CopyFileData;
import pojo.ResponseErrorData;
import pojo.ResponseHrefData;
import pojo.UserData;

import java.util.Map;

import static helpers.BaseRequests.*;

public class UploadAndCopyFileTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        createResource(RESOURCES_PATH, Map.of("path", INPUT_FOLDER), 201);
        createResource(RESOURCES_PATH, Map.of("path", OUTPUT_FOLDER), 201);
    }

    @AfterMethod
    public void tearDown() {
        deleteResource(RESOURCES_PATH, Map.of("path", INPUT_FOLDER), 202);
        deleteResource(RESOURCES_PATH, Map.of("path", OUTPUT_FOLDER), 202);
    }

    @Test
    public void uploadAndCopyFileTest() {
        //получаем ссылку для загрузки файла
        Response responseLinkForUploadFile = getResource(RESOURCES_UPLOAD, Map.of("path", INPUT_FOLDER + "/" + FILE_NAME), 200);
        ResponseHrefData downloadLink = JsonUtils.fromJson(responseLinkForUploadFile.asString(), ResponseHrefData.class);
        String hrefUploadFile = downloadLink.getHref();

        //загружаем по ссылке для загрузки файл с переданным текстом
        UserData userData = new UserData(USER_NAME, PASSWORD);
        String jsonText = JsonUtils.toJson(userData);
        createFile(hrefUploadFile, 201, jsonText);

        //копируем файл из одной папки в другую
        Response responseFirstCopyFile = sendPostRequestWithAuth(RESOURCES_COPY, Map.of("from", "disk:/" + INPUT_FOLDER + "/" + FILE_NAME, "path", "disk:/" + OUTPUT_FOLDER + "/" + FILE_NAME), 201);
        CopyFileData copyResponse = JsonUtils.fromJson(responseFirstCopyFile.asString(), CopyFileData.class);

        Assert.assertEquals(copyResponse.getMethod(), "GET");
        Assert.assertNotNull(copyResponse.getHref());
        Assert.assertFalse(copyResponse.isTemplated());

        //копируем файл из одной папки в другую повторно
        Response responseSecondCopyFile = sendPostRequestWithAuth(RESOURCES_COPY, Map.of("from", "disk:/" + INPUT_FOLDER + "/" + FILE_NAME, "path", "disk:/" + OUTPUT_FOLDER + "/" + FILE_NAME), 409);
        ResponseErrorData errorResponse = JsonUtils.fromJson(responseSecondCopyFile.asString(), ResponseErrorData.class);

        Assert.assertNotNull(errorResponse.getError());
        Assert.assertNotNull(errorResponse.getDescription());
        Assert.assertNotNull(errorResponse.getMessage());
    }

    /**
     * Отправляет POST-запрос с авторизацией и проверяет статус.
     */
    public Response sendPostRequestWithAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendPostRequest(true, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет PUT-запрос без авторизации и проверяет статус.
     */
    public void createFile(String endpoint, int expectedStatus, String bodyData) {
        sendPutRequestWithBody(endpoint, expectedStatus, bodyData);
    }
}