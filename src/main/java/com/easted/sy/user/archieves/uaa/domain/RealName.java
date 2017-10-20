package com.easted.sy.user.archieves.uaa.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "real_name", schema = "uaa", catalog = "")
public class RealName {
    private int id;
    private String login;
    private String name;
    private String tel;
    private String frontImage;
    private String backImage;
    private String selfieImage;
    private String identity;
    private String state;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "front_image")
    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    @Basic
    @Column(name = "back_image")
    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    @Basic
    @Column(name = "selfie_image")
    public String getSelfieImage() {
        return selfieImage;
    }

    public void setSelfieImage(String selfieImage) {
        this.selfieImage = selfieImage;
    }


    @Basic
    @Column(name = "identity")
    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealName that = (RealName) o;
        return id == that.id &&
            Objects.equals(login, that.login) &&
            Objects.equals(name, that.name) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(frontImage, that.frontImage) &&
            Objects.equals(backImage, that.backImage) &&
            Objects.equals(selfieImage, that.selfieImage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, login, name, tel, frontImage, backImage, selfieImage);
    }
}
