package appointmenthospital.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
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
