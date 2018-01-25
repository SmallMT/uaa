package com.easted.sy.user.archieves.uaa.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bind_enterprise", schema = "uaa", catalog = "")
public class BindEnterprise {
    private int id;
    private String enterpriseName;
    private String creditCode;

    private User user;

    private String businessLicense;

    private String state;

    private Boolean isLegalPerson;

    private List<BindAgent> bindAgentList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "enterprise_name")
    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    @Basic
    @Column(name = "credit_code")
    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @OneToMany(mappedBy = "bindEnterprise")
    public List<BindAgent> getBindAgentList() {
        return bindAgentList;
    }

    public void setBindAgentList(List<BindAgent> bindAgentList) {
        this.bindAgentList = bindAgentList;
    }

    @Basic
    @Column(name = "business_license")
    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    @Basic
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "is_legalPerson")
    public Boolean getLegalPerson() {
        return isLegalPerson;
    }

    public void setLegalPerson(Boolean legalPerson) {
        isLegalPerson = legalPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BindEnterprise that = (BindEnterprise) o;

        if (id != that.id) return false;
        if (enterpriseName != null ? !enterpriseName.equals(that.enterpriseName) : that.enterpriseName != null)
            return false;
        if (creditCode != null ? !creditCode.equals(that.creditCode) : that.creditCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (enterpriseName != null ? enterpriseName.hashCode() : 0);
        result = 31 * result + (creditCode != null ? creditCode.hashCode() : 0);
        return result;
    }
}
