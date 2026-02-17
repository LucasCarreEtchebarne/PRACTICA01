package com.PRACTICA01.demo.service;

import com.PRACTICA01.demo.domain.Arbol;
import com.PRACTICA01.demo.repository.ArbolRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArbolService {

    private final ArbolRepository arbolRepository;
    private final StorageService storageService; // lo hacemos ahorita

    public ArbolService(ArbolRepository arbolRepository, StorageService storageService) {
        this.arbolRepository = arbolRepository;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public List<Arbol> getArboles() {
        return arbolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Arbol> getArbol(Integer idArbol) {
        return arbolRepository.findById(idArbol);
    }

    @Transactional
    public void save(Arbol arbol, MultipartFile imagenFile) {
        // 1) Guarda primero para obtener id
        arbol = arbolRepository.save(arbol);

        // 2) Si viene imagen, la subimos y guardamos la ruta/url
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String ruta = storageService.store(imagenFile, arbol.getIdArbol());
                arbol.setRutaImagen(ruta);
                arbolRepository.save(arbol);
            } catch (IOException e) {
                // opcional: podrías loggear
            }
        }
    }

    @Transactional
    public void delete(Integer idArbol) {
        if (!arbolRepository.existsById(idArbol)) {
            throw new IllegalArgumentException("El árbol con ID " + idArbol + " no existe.");
        }

        try {
            arbolRepository.deleteById(idArbol);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el árbol. Tiene datos asociados.", e);
        }
    }
}

