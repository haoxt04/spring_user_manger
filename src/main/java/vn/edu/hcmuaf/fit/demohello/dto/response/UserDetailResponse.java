package vn.edu.hcmuaf.fit.demohello.dto.response;

import lombok.*;
import vn.edu.hcmuaf.fit.demohello.utils.Gender;
import vn.edu.hcmuaf.fit.demohello.utils.UserStatus;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDetailResponse implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    public UserDetailResponse(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
