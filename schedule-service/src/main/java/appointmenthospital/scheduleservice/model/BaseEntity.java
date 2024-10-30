package appointmenthospital.scheduleservice.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
