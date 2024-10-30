package appointmenthospital.infoservice.model.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.*;

import java.sql.Timestamp;


@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends DataInfoTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false)
    protected Long id;

    public BaseEntity(Long id, Timestamp createAt, Timestamp updateAt) {
        super(createAt, updateAt);
        this.id=id;
    }

    public BaseEntity() {
        super();

    }
}
