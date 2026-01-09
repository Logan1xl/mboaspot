package com.example.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Contrôleur pour gérer l'upload des fichiers images
 * @author MboaSpot Team
 */
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    /**
     * Upload une seule image
     * POST /api/upload/image
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("Upload d'une image: {}", file.getOriginalFilename());
        try {
            String validationError = validateFile(file);
            if (validationError != null) {
                logger.warn("Validation échouée pour le fichier {} : {}", file.getOriginalFilename(), validationError);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse(validationError));
            }

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "." + extension;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> response = new HashMap<>();
            response.put("url", "/uploads/" + fileName);
            response.put("fileName", fileName);
            response.put("originalName", originalFilename);
            response.put("size", String.valueOf(file.getSize()));

            logger.info("Image uploadée avec succès: {}", fileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Erreur lors de l'upload de l'image {}: {}", file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la sauvegarde du fichier: " + e.getMessage()));
        }
    }

    /**
     * Upload multiple images
     * POST /api/upload/images
     */
    @PostMapping("/images")
    public ResponseEntity<?> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        logger.info("Upload multiple d'images, nombre de fichiers: {}", files.length);
        List<Map<String, String>> uploadedFiles = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String validationError = validateFile(file);
                if (validationError != null) {
                    errors.add(file.getOriginalFilename() + ": " + validationError);
                    logger.warn("Validation échouée pour le fichier {} : {}", file.getOriginalFilename(), validationError);
                    continue;
                }

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = file.getOriginalFilename();
                String extension = getFileExtension(originalFilename);
                String fileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "." + extension;
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Map<String, String> fileInfo = new HashMap<>();
                fileInfo.put("url", "/uploads/" + fileName);
                fileInfo.put("fileName", fileName);
                fileInfo.put("originalName", originalFilename);

                uploadedFiles.add(fileInfo);
                logger.info("Image uploadée avec succès: {}", fileName);

            } catch (IOException e) {
                errors.add(file.getOriginalFilename() + ": Erreur sauvegarde");
                logger.error("Erreur lors de l'upload du fichier {}: {}", file.getOriginalFilename(), e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("uploaded", uploadedFiles);
        response.put("errors", errors);
        response.put("totalUploaded", uploadedFiles.size());
        response.put("totalErrors", errors.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer une image
     * DELETE /api/upload/image/{fileName}
     */
    @DeleteMapping("/image/{fileName}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {
        logger.info("Suppression de l'image: {}", fileName);
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

            if (!Files.exists(filePath)) {
                logger.warn("Fichier non trouvé: {}", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Fichier non trouvé"));
            }

            Files.delete(filePath);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Fichier supprimé avec succès");
            response.put("fileName", fileName);

            logger.info("Fichier supprimé avec succès: {}", fileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Erreur lors de la suppression de l'image {}: {}", fileName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur lors de la suppression: " + e.getMessage()));
        }
    }

    // ========== MÉTHODES PRIVÉES DE VALIDATION ==========

    private String validateFile(MultipartFile file) {
        if (file.isEmpty()) return "Le fichier est vide";
        if (file.getSize() > MAX_FILE_SIZE) return "Le fichier est trop volumineux (max 5MB)";
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) return "Type de fichier non autorisé. Formats acceptés: JPG, PNG, GIF, WEBP";
        String filename = file.getOriginalFilename();
        if (filename == null) return "Nom de fichier invalide";
        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) return "Extension de fichier non autorisée";
        return null;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
