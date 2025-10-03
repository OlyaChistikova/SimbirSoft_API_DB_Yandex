package tests;

import helpers.BaseRequests;

import static io.restassured.RestAssured.given;

public class BaseTest {

    /**
     * Токен авторизации для доступа к API.
     */
    protected static final String TOKEN = BaseRequests.TOKEN;

    /**
     * Путь для взаимодействия с ресурсами.
     */
    protected static final String RESOURCES_PATH = BaseRequests.RESOURCES_PATH;

    /**
     * Путь для взаимодействия с ресурсами корзины.
     */
    protected static final String TRASH_RESOURCES_PATH = BaseRequests.TRASH_RESOURCES_PATH;

    /**
     * Выполняет запрос на создание папки по указанному названию.
     *
     * @param pathData название папки, которую необходимо создать.
     */
    public void createFolderRequest(String pathData) {
        given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .put(RESOURCES_PATH)
                .then()
                .statusCode(201)
                .extract().response();
    }

    /**
     * Выполняет запрос на удаление папки по указанному названию.
     *
     * @param pathData название папки, которую необходимо удалить.
     */
    public void deleteFolderRequest(String pathData) {
        given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .param("path", pathData)
                .when()
                .delete(RESOURCES_PATH)
                .then()
                .statusCode(204)
                .extract().response();
    }

    /**
     * Очищает корзину, удаляя все папки внутри
     */
    public void cleanTrashResourcesAfterDelete() {
        given()
                .spec(BaseRequests.requestSpec(TOKEN))
                .when()
                .delete(TRASH_RESOURCES_PATH)
                .then()
                .statusCode(202)
                .extract().response();
    }
}