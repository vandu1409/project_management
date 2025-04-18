package com.project_managament.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileStorageUtil {

    public static String saveFile(String uploadDirPath, String filename, byte[] data) throws IOException {
        Path directory = Paths.get(uploadDirPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(filename);
        Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return filePath.toString().replace("\\", "/");
    }

    public static boolean deleteFile(String absolutePath) {
        try {
            return Files.deleteIfExists(Paths.get(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] readFile(String absolutePath) throws IOException {
        return Files.readAllBytes(Paths.get(absolutePath));
    }

    public static boolean fileExists(String absolutePath) {
        return Files.exists(Paths.get(absolutePath));
    }
}
