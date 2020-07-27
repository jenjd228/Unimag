package DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    private String email;

    private String password;

    private String fio;

    private String birthday;

    private Integer points;

    private String university;

    private Date registrationDate;

    private Date maxDate;

    private String secureKod;
}
