package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    /**
     * Загружает файл по URL, сохраняет его по указанному пути и возвращает десериализованный объект RequestUserData.
     *
     * @param hrefDownloadFile URL файла для скачивания.
     * @param outputFilePath   Путь к файлу для сохранения.
     * @return Объект RequestUserData, десериализованный из скачанного файла.
     */
    public static UserData downloadFile(String hrefDownloadFile, String outputFilePath) {
        try {
            URL url = new URL(hrefDownloadFile);
            // Скачивание файла и сохранение по указанному пути
            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, Paths.get(outputFilePath));
            }
            // После скачивания десериализация содержимого файла
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Paths.get(outputFilePath).toFile(), UserData.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при скачивании или десериализации файла: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет файл по заданному пути.
     *
     * @param filePath путь к удаляемому файлу.
     */
    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при удалении файла: " + e.getMessage(), e);
        }
    }
}