package appointmenthospital.filestorage.service;

import appointmenthospital.filestorage.exc.GenericErrorResponse;
import appointmenthospital.filestorage.model.File;
import appointmenthospital.filestorage.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final FileRepository fileRepository;
    private String FOLDER_PATH;

    @PostConstruct
    public void init() {
        String currentWorkingDirectory = System.getProperty("user.dir");

        FOLDER_PATH = currentWorkingDirectory + "/file-storage/src/main/resources/documents";

        java.io.File targetFolder = new java.io.File(FOLDER_PATH);

        if (!targetFolder.exists()) {
            boolean directoriesCreated = targetFolder.mkdirs();
            if (!directoriesCreated) {
                throw GenericErrorResponse.builder()
                        .message("Unable to create directories")
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
    }

    public String uploadDocumentToFileSystem(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String filePath = FOLDER_PATH + "/" + uuid;

        try {
            file.transferTo(new java.io.File(filePath));
        } catch (IOException e) {
            throw GenericErrorResponse.builder()
                    .message("Unable to save file to storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        fileRepository.save(File.builder()
                .id(uuid)
                .type(file.getContentType())
                .filePath(filePath).build());
        return uuid;
    }

    public byte[] downloadDocumentFromFileSystem(String id) {
        try {
            return Files.readAllBytes(new java.io.File(findFileById(id)
                    .getFilePath()).toPath());
        } catch (IOException e) {
            throw GenericErrorResponse.builder()
                    .message("Unable to read file from storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public void deleteDocumentFromFileSystem(String id) {
        java.io.File file = new java.io.File(findFileById(id).getFilePath());

        boolean deletionResult = file.delete();

        if (deletionResult) fileRepository.deleteById(id);

        else throw GenericErrorResponse.builder()
                .message("Unable to delete file from storage")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }


    protected File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> GenericErrorResponse.builder()
                        .message("File not found")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }
}
