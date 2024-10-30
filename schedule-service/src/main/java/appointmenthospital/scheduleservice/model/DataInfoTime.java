package appointmenthospital.scheduleservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class DataInfoTime {

    @CreatedDate
    @Column(name = "createAt")
    private Timestamp createAt;
    @LastModifiedDate
    @Column(name = "updateAt")
    private Timestamp updateAt;
    @Version
    protected int version;
    public DataInfoTime(Timestamp createAt, Timestamp updateAt) {
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public DataInfoTime() {
    }
}
