package tests;

import helpers.JsonUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo.ResponseHrefData;
import pojo.UserData;

import java.util.Map;

import static helpers.BaseRequests.*;
import static helpers.FileHelper.deleteFile;
import static helpers.FileHelper.downloadFile;

public class DownloadTextFileTest extends BaseTest {
    String jsonTextInput;

    @BeforeMethod
    public void setUp() {
        createResource(RESOURCES_PATH, Map.of("path", SDET_FOLDER), 201);

        //получаем ссылку для загрузки файла
        Response responseLinkForUploadFile = getResource(RESOURCES_UPLOAD, Map.of("path", SDET_FOLDER + "/" + FILE_NAME), 200);
        ResponseHrefData downloadLink = JsonUtils.fromJson(responseLinkForUploadFile.asString(), ResponseHrefData.class);
        String hrefUploadFile = downloadLink.getHref();

        //загружаем файл по ссылке с JSON содержимым
        UserData userData = new UserData(USER_NAME, PASSWORD);
        jsonTextInput = JsonUtils.toJson(userData);
        createFile(hrefUploadFile, 201, jsonTextInput);
    }

    @AfterMethod
    public void tearDown() {
        deleteResource(RESOURCES_PATH, Map.of("path", SDET_FOLDER), 202);
    }

    @Test
    public void downloadTextFileTest() {
        //получаем ссылку для скачивания файла
        Response responseLinkForDownloadFile = getResource(RESOURCES_DOWNLOAD, Map.of("path", SDET_FOLDER + "/" + FILE_NAME), 200);
        ResponseHrefData responseHrefData = JsonUtils.fromJson(responseLinkForDownloadFile.asString(), ResponseHrefData.class);
        String hrefDownloadFile = responseHrefData.getHref();

        Assert.assertNotNull(hrefDownloadFile);

        //скачиваем файл и десериализуем содержимое в объект UserData
        UserData userData = downloadFile(hrefDownloadFile, OUTPUT_FILE_PATH);
        String jsonTextOutput = JsonUtils.toJson(userData);

        Assert.assertEquals(jsonTextOutput, jsonTextInput, "Загруженный файл не совпадает с исходным");

        deleteFile(OUTPUT_FILE_PATH);
    }

    /**
     * Отправляет PUT-запрос без авторизации и проверяет статус.
     */
    public void createFile(String endpoint, int expectedStatus, String bodyData) {
        sendPutRequestWithBody(endpoint, expectedStatus, bodyData);
    }
}