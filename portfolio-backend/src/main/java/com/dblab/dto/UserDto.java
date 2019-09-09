package com.dblab.dto;

import com.dblab.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
public class UserDto {

    @NotBlank(message = "필수 항목입니다.")
    @Size(min = 4, max = 12, message = "아이디는 4자 이상 12장 이하입니다.")
    private String username;

    @NotBlank(message = "필수 항목입니다.")
    @Size(min = 6, max = 16, message = "비밀번호는 4자 이상 12자 이하입니다.")
    private String password;

    @NotBlank(message = "필수 항목입니다.")
    @Email(message = "이메일의 양식을 지켜주세요.")
    private String email;

    public User setUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setRegisteredDate(LocalDateTime.now());
        return user;
    }
}
