package appointmenthospital.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-storage", url = "http://localhost:10003", path = "api/v1/file-storage")
public interface FileStorageClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadImageToFileSystem(@RequestPart("image") MultipartFile file);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteImageFromFileSystem(@PathVariable String id);
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id);

    @PostMapping("/document/upload")
    public ResponseEntity<String> uploadDocumentToFIleSystem(@RequestPart("document") MultipartFile file);

    @GetMapping("/document/download/{id}")
    public ResponseEntity<?> downloadDocumentFromFileSystem(@PathVariable String id);
    @DeleteMapping("/document/delete/{id}")
    public ResponseEntity<Void> deleteDocumentFromFileSystem(@PathVariable String id);

}