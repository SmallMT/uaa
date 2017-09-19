package com.easted.sy.user.archieves.uaa.web.view.vm;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterVM {

    @Size(min = 1, max = 50,message = "用户名位数为1到50位")
    @NotNull(message = "用户名为不为")
    private String login;

    private String username;

    @Email(message = "请输入有效的电子邮箱")
    @NotNull(message = "电子邮件不为空")
    private String email;

    @Size(min = 4,max = 100,message = "密码位数为4到100位")
    @NotNull(message = "密码不为空")
    private String password;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
