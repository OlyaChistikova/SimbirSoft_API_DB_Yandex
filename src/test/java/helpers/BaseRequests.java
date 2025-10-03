package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseRequests {

    /**
     * Токен авторизации для доступа к API.
     */
    public static final String TOKEN = ParametersProvider.getProperty("token");

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
     * Данные для авторизации
     */
    public static final String LOGIN = ParametersProvider.getProperty("username");

    /**
     * Путь для взаимодействия с диском.
     */
    public static final String DISK_PATH = ParametersProvider.getProperty("disk_path");


    public static RequestSpecification requestSpec(String authToken) {
        return new RequestSpecBuilder()
                .setBaseUri(ParametersProvider.getProperty("apiUrl"))
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "OAuth " + authToken)
                .build();
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ParametersProvider.getProperty("apiUrl"))
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Выполняет запрос на добавление папки по указанному названию с авторизацией.
     *
     * @param pathData название папки, которую необходимо добавить.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response addFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .put(RESOURCES_PATH)
                .then()
                .statusCode(201)
                .extract().response();
    }

    /**
     * Выполняет запрос на добавление папки с некорректными параметрами
     *
     * @param pathData название папки, который передается в запрос. Может содержать некорректное или пустое значение.
     * @return объект Response, содержащий ответ сервера
     */
    public static Response addFolderIncorrectParamsRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .put(RESOURCES_PATH)
                .then()
                .statusCode(400)
                .extract().response();
    }

    /**
     * Выполняет запрос на добавление папки с недопустимыми параметрами
     *
     * @param pathData название папки, который передается в запрос. Может содержать несуществующие значения.
     * @return объект Response, содержащий ответ сервера
     */
    public static Response addFolderInvalidParamsRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .put(RESOURCES_PATH)
                .then()
                .statusCode(404)
                .extract().response();
    }

    /**
     * Выполняет запрос на добавление папки без авторизации.
     *
     * @param pathData название папки, которую необходимо добавить.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response addFolderWithoutAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec())
                .param("path", pathData)
                .when()
                .put(RESOURCES_PATH)
                .then()
                .statusCode(401)
                .extract().response();
    }

    /**
     * Выполняет запрос на получение папки по ее названию.
     *
     * @param pathData название папки для получения информации.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response getFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .get(RESOURCES_PATH)
                .then()
                .statusCode(200)
                .extract().response();
    }

    /**
     * Выполняет запрос на получение несуществующей папки с авторизацией.
     *
     * @param pathData название папки, которой не существует.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response getUnExistentFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .get(RESOURCES_PATH)
                .then()
                .statusCode(404)
                .extract().response();
    }

    /**
     * Выполняет запрос на получение всех имен папок с авторизацией.
     *
     * @return список имен папок
     */
    public static List<String> getAllNamesFoldersRequest() {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", "disk:/")
                .when()
                .get(RESOURCES_PATH)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("_embedded.items.name");
    }

    /**
     * Выполняет запрос на удаление папки по имени папки с авторизацией.
     *
     * @param pathData название папки для удаления.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response deleteFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .delete(RESOURCES_PATH)
                .then()
                .statusCode(204)
                .extract().response();
    }

    /**
     * Выполняет запрос на удаление уже удаленной папки.
     *
     * @param pathData название папки, которая уже удалена.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response deleteAlreadyDeletedFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .delete(RESOURCES_PATH)
                .then()
                .statusCode(404)
                .extract().response();
    }

    /**
     * Выполняет запрос на удаление папки без авторизации.
     *
     * @param pathData название папки для удаления.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response deleteFolderWithoutAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec())
                .param("path", pathData)
                .when()
                .delete(RESOURCES_PATH)
                .then()
                .statusCode(401)
                .extract().response();
    }

    /**
     * Выполняет запрос для получения корзины с удаленными папками с авторизацией.
     *
     * @return список папок в корзине.
     */
    public static List<Map<String, Object>> checkTrashFoldersAuthRequest() {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path")
                .when()
                .get(TRASH_RESOURCES_PATH)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("_embedded.items");
    }

    /**
     * Выполняет запрос для восстановления папки по названию с авторизацией.
     *
     * @param pathData название папки для восстановления.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response returnFolderAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .put(RETURN_RESOURCES_PATH)
                .then()
                .statusCode(201)
                .extract().response();
    }

    /**
     * Выполняет запрос для восстановления папки по названию без авторизации.
     *
     * @param pathData название папки для восстановления.
     * @return объект Response, содержащий ответ сервера.
     */
    public static Response returnFolderWithoutAuthRequest(String pathData) {
        return given()
                .spec(BaseRequests.requestSpec())
                .param("path", pathData)
                .when()
                .put(RETURN_RESOURCES_PATH)
                .then()
                .statusCode(401)
                .extract().response();
    }
}