package appointmenthospital.thymeleafclient.model.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Patient{
    private List<Profile> profiles;
    private User user;
}
