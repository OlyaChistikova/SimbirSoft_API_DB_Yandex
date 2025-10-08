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
    public static final String LOGIN = ParametersProvider.getProperty("login");

    /**
     * Путь для взаимодействия с диском.
     */
    public static final String DISK_PATH = ParametersProvider.getProperty("disk_path");

    /**
     * Путь для загрузки ресурсов
     */
    public static final String RESOURCES_UPLOAD = ParametersProvider.getProperty("resources_upload");

    /**
     * Путь для копирования ресурсов.
     */
    public static final String RESOURCES_COPY = ParametersProvider.getProperty("resources_copy");

    /**
     * Путь для скачивания ресурсов.
     */
    public static final String RESOURCES_DOWNLOAD = ParametersProvider.getProperty("resources_download");

    /**
     * Папка для входных файлов.
     */
    public static final String INPUT_FOLDER = ParametersProvider.getProperty("inputFolder");

    /**
     * Папка для выходных файлов.
     */
    public static final String OUTPUT_FOLDER = ParametersProvider.getProperty("outputFolder");

    /**
     * Папка SDET.
     */
    public static final String SDET_FOLDER = ParametersProvider.getProperty("sdetFolder");

    /**
     * Имя файла для теста.
     */
    public static final String FILE_NAME = ParametersProvider.getProperty("fileName");

    /**
     * Имя пользователя для теста.
     */
    public static final String USER_NAME = ParametersProvider.getProperty("username");

    /**
     * Пароль пользователя для теста.
     */
    public static final String PASSWORD = ParametersProvider.getProperty("password");

    /**
     * Путь к файлу для скачивания.
     */
    public static final String OUTPUT_FILE_PATH = ParametersProvider.getProperty("outputFilePath");

    /**
     * Создает конфигурацию запроса с авторизацией.
     *
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
     *
     * @return объект RequestSpecification с настройками для неавторизованного запроса.
     */
    private static RequestSpecification getRequestSpecWithoutAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(API_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Создает конфигурацию запроса без базового URL, только с содержимым и авторизацией.
     *
     * @return объект RequestSpecification без базового URL.
     */
    private static RequestSpecification getRequestSpecWithoutBaseUrl() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "OAuth " + TOKEN)
                .build();
    }

    /**
     * Отправляет GET-запрос к API.
     *
     * @param withAuth       указывает, нужно ли использовать авторизацию.
     * @param endpoint       относительный путь API.
     * @param params         параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    public static Response sendGetRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
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
     *
     * @param withAuth       указывает, нужно ли использовать авторизацию.
     * @param endpoint       относительный путь API.
     * @param params         параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    public static Response sendPutRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
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
     * Отправляет PUT-запрос с телом без авторизации и без базового URL.
     *
     * @param endpoint       относительный путь API.
     * @param expectedStatus ожидаемый HTTP статус.
     * @param bodyData       содержимое тела запроса.
     */
    public static void sendPutRequestWithBody(String endpoint, int expectedStatus, String bodyData) {
        given()
                .spec(getRequestSpecWithoutBaseUrl())
                .body(bodyData)
                .when()
                .put(endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();
    }

    /**
     * Отправляет DELETE-запрос к API.
     *
     * @param withAuth       указывает, нужно ли использовать авторизацию.
     * @param endpoint       относительный путь API.
     * @param params         параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    public static Response sendDeleteRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
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
     * Отправляет POST-запрос к API.
     *
     * @param withAuth       указывает, нужно ли использовать авторизацию.
     * @param endpoint       относительный путь API.
     * @param params         параметры запроса.
     * @param expectedStatus ожидаемый HTTP статус ответа.
     * @return объект Response с ответом сервера.
     */
    public static Response sendPostRequest(boolean withAuth, String endpoint, Map<String, String> params, int expectedStatus) {
        return given()
                .spec(withAuth ? getRequestSpecWithAuth() : getRequestSpecWithoutAuth())
                .queryParams(params != null ? params : Map.of())
                .when()
                .post(endpoint)
                .then()
                .statusCode(expectedStatus)
                .extract().response();
    }
}