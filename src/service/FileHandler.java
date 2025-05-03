package service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
    public String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public void writeFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }

    public List<String> batchReadFiles(String folderPath) throws IOException {
        List<String> contents = new ArrayList<>();
        Files.list(Paths.get(folderPath)).filter(Files::isRegularFile).forEach(file -> {
            try {
                contents.add(readFile(file.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return contents;
    }
}

