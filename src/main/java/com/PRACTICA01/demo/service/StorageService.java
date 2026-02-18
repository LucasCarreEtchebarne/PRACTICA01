package com.PRACTICA01.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private final Path root = Paths.get("src/main/resources/static/img/arbol");

    public String store(MultipartFile file, Integer idArbol) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String original = file.getOriginalFilename();
        String ext = "";

        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf(".")); // .jpg .png
        }

        String fileName = "img" + String.format("%014d", idArbol) + ext;
        Path destino = root.resolve(fileName);

        Files.copy(file.getInputStream(), destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Esta es la ruta que se usa en HTML (URL)
        return "/img/arbol/" + fileName;
    }
}

