package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseRequests {

    /**
     * Токен авторизации для доступа к API.
     */
    public static final String TOKEN = ParametersProvider.getProperty("token");

    /**
     * Базовый URL API.
     */
    public static final String API_URL = ParametersProvider.getProperty("apiUrl");

    /**
     * Путь для взаимодействия с ресурсами.
     */
    public static final String RESOURCES_PATH = ParametersProvider.getProperty("resources_path");

    /**
     * Путь для взаимодействия с ресурсами корзины.
     */
    public static final String TRASH_RESOURCES_PATH = ParametersProvider.getProperty("trash_resources_path");

    /**
     * Путь для восстановления ресурсов из корзины.
     */
    public static final String RETURN_RESOURCES_PATH = ParametersProvider.getProperty("return_resources_path");

    /**
     * Имя пользователя для авторизации.
     */
    public static final String LOGIN = ParametersProvider.getProperty("username");

    /**
     * Путь для взаимодействия с диском.
     */
    public static final String DISK_PATH = ParametersProvider.getProperty("disk_path");

    /**
     * Создает конфигурацию запроса с авторизацией.
     * @return объект RequestSpecification с настройками для авторизованного запроса.
     */
    private static RequestSpecification getRequestSpecWithAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(API_URL)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();
    }

    /**
     * Создает конфигурацию запроса без авторизации.
     * @return объект RequestSpecification с настройками для неавторизованного запроса.
     */
    private static RequestSpecification getRequestSpecWithoutAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(API_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Отправляет GET-запрос к API.
     * @param withAuth указывает, нужно ли использовать авторизацию.
     * @param endpoint относительный путь API.
     * @param params параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    private static Response sendGetRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
        return given()
                .spec(withAuth ? getRequestSpecWithAuth() : getRequestSpecWithoutAuth())
                .params(params != null ? params : Map.of())
                .when()
                .get(endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();
    }

    /**
     * Отправляет PUT-запрос к API.
     * @param withAuth указывает, нужно ли использовать авторизацию.
     * @param endpoint относительный путь API.
     * @param params параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    private static Response sendPutRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
        return given()
                .spec(withAuth ? getRequestSpecWithAuth() : getRequestSpecWithoutAuth())
                .params(params != null ? params : Map.of())
                .when()
                .put(endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();
    }

    /**
     * Отправляет DELETE-запрос к API.
     * @param withAuth указывает, нужно ли использовать авторизацию.
     * @param endpoint относительный путь API.
     * @param params параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    private static Response sendDeleteRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
        return given()
                .spec(withAuth ? getRequestSpecWithAuth() : getRequestSpecWithoutAuth())
                .params(params != null ? params : Map.of())
                .when()
                .delete(endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();
    }

    /**
     * Отправляет GET-запрос с авторизацией и проверяет статус.
     */
    public static Response sendGetRequestWithAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendGetRequest(true, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет GET-запрос без авторизации и проверяет статус.
     */
    public static Response sendGetRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendGetRequest(false, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет PUT-запрос с авторизацией и проверяет статус.
     */
    public static Response sendPutRequestWithAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendPutRequest(true, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет PUT-запрос без авторизации и проверяет статус.
     */
    public static Response sendPutRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendPutRequest(false, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет DELETE-запрос с авторизацией и проверяет статус.
     */
    public static Response sendDeleteRequestWithAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendDeleteRequest(true, endpoint, params, expectedStatus);
    }

    /**
     * Отправляет DELETE-запрос без авторизации и проверяет статус.
     */
    public static Response sendDeleteRequestWithoutAuth(String endpoint, Map<String, String> params, int expectedStatus) {
        return sendDeleteRequest(false, endpoint, params, expectedStatus);
    }
}