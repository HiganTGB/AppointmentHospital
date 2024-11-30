package appointmenthospital.filestorage.controller;

import appointmenthospital.filestorage.service.DocumentService;
import appointmenthospital.filestorage.service.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file-storage")
@RequiredArgsConstructor
@Tag(name = "File Storage API", description = "All about image")
public class StorageController {
    private final StorageService storageService;
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImageToFIleSystem(@RequestPart("image") MultipartFile file) {
        return ResponseEntity.ok().body(storageService.uploadImageToFileSystem(file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(storageService.downloadImageFromFileSystem(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImageFromFileSystem(@PathVariable String id) {
        storageService.deleteImageFromFileSystem(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/document/upload")
    public ResponseEntity<String> uploadDocumentToFIleSystem(@RequestPart("document") MultipartFile file) {
        return ResponseEntity.ok().body(documentService.uploadDocumentToFileSystem(file));
    }

    @GetMapping("/document/download/{id}")
    public ResponseEntity<?> downloadDocumentFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(documentService.downloadDocumentFromFileSystem(id));
    }
    @DeleteMapping("/document/delete/{id}")
    public ResponseEntity<Void> deleteDocumentFromFileSystem(@PathVariable String id) {
        documentService.deleteDocumentFromFileSystem(id);
        return ResponseEntity.ok().build();
    }




}