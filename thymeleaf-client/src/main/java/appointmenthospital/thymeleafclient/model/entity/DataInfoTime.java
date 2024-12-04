package appointmenthospital.thymeleafclient.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public abstract class DataInfoTime {
    private Timestamp createAt;
    private Timestamp updateAt;
    protected int version;
    public DataInfoTime(Timestamp createAt, Timestamp updateAt) {
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    public DataInfoTime() {

    }
}
