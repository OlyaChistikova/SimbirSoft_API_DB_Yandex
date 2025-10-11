package tests;

import helpers.JsonUtils;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static helpers.BaseRequests.*;

public class GetListFilesTest extends BaseTest{
    private static final long FOLDER_COUNT = 10;

    @BeforeMethod
    public void setUp(){
        for (int i = 0; i < FOLDER_COUNT; i++) {
            createResource(RESOURCES_PATH, Map.of("path", SDET_FOLDER + i), 201);
        }
    }

    @AfterMethod
    public void tearDown(){
        for (int i = 0; i < FOLDER_COUNT; i++) {
            deleteResource(RESOURCES_PATH, Map.of("path", SDET_FOLDER + i), 204);
        }
        deleteResource(TRASH_RESOURCES_PATH, null, 202);
    }

    @Test
    public void getListFilesTest(){
        Response response = getResource(RESOURCES_FILES, Map.of("limit", FOLDER_COUNT), 200);

        String stringResponse = response.getBody().asString();
        JsonUtils.validateJsonAgainstSchema(stringResponse, JSON_SCHEMA);
    }
}