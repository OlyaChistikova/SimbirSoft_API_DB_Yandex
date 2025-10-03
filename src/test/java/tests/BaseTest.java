package tests;

import helpers.ParametersProvider;

public class BaseTest {

    /**
     * Токен авторизации для доступа к API.
     */
    protected static final String TOKEN = ParametersProvider.getProperty("token");

    /**
     * Путь для взаимодействия с диском.
     */
    protected static final String DISK_PATH = ParametersProvider.getProperty("disk_path");

    /**
     * Данные для авторизации
     */
    protected static final String LOGIN = ParametersProvider.getProperty("username");
}