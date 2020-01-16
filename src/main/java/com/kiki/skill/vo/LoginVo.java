package com.kiki.skill.vo;

import com.kiki.skill.util.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;

    public LoginVo() {
    }

    public LoginVo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
