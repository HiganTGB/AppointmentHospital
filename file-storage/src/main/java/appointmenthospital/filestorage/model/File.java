package appointmenthospital.filestorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class File {
    @Id
    private String id;
    private String type;
    private String filePath;
}