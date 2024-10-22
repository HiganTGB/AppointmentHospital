package appointmenthospital.filestorage.repository;

import appointmenthospital.filestorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}