package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;

import java.util.Map;

public class BaseTest {

    /**
     * Путь для взаимодействия с ресурсами.
     */
    protected static final String RESOURCES_PATH = BaseRequests.RESOURCES_PATH;

    /**
     * Путь для взаимодействия с ресурсами корзины.
     */
    protected static final String TRASH_RESOURCES_PATH = BaseRequests.TRASH_RESOURCES_PATH;

    /**
     * Создает ресурс по указанному пути с авторизацией.
     * @param endpoint API endpoint для ресурса.
     * @param params название или путь ресурса.
     * @param expectedStatus ожидаемый статус код.
     */
    public Response createResource(String endpoint, Map<String, String> params, int expectedStatus) {
        return BaseRequests.sendPutRequestWithAuth(endpoint, params, expectedStatus);
    }

    /**
     * Удаляет ресурс по указанному пути с авторизацией.
     * @param endpoint API endpoint для ресурса.
     * @param params название или путь ресурса.
     * @param expectedStatus ожидаемый статус код.
     */
    public Response deleteResource(String endpoint, Map<String, String> params, int expectedStatus) {
        return BaseRequests.sendDeleteRequestWithAuth(endpoint, params, expectedStatus);
    }

    /**
     * Получает ресурс по-указанному endpoint.
     * @param endpoint API endpoint для ресурса.
     * @param params название или путь ресурса.
     * @param expectedStatus ожидаемый статус код.
     */
    public Response getResource(String endpoint, Map<String, String> params, int expectedStatus) {
        return BaseRequests.sendGetRequestWithAuth(endpoint, params, expectedStatus);
    }
}