package backend.checkmate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemberForm {

    private String email;
    private String name;
    private String password;

}
