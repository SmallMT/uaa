package com.easted.sy.user.archieves.uaa.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client_details", schema = "uaa", catalog = "")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ClientDetails {
    private String appId;
    private String resourceIds;
    private String appSecret;
    private String scope;
    private String grantTypes;
    private String redirectUrl;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApproveScopes;

    @Id
    @Column(name = "appId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Basic
    @Column(name = "resourceIds")
    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Basic
    @Column(name = "appSecret")
    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Basic
    @Column(name = "scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Basic
    @Column(name = "grantTypes")
    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    @Basic
    @Column(name = "redirectUrl")
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Basic
    @Column(name = "authorities")
    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Basic
    @Column(name = "access_token_validity")
    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    @Basic
    @Column(name = "refresh_token_validity")
    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    @Basic
    @Column(name = "additionalInformation")
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Basic
    @Column(name = "autoApproveScopes")
    public String getAutoApproveScopes() {
        return autoApproveScopes;
    }

    public void setAutoApproveScopes(String autoApproveScopes) {
        this.autoApproveScopes = autoApproveScopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDetails that = (ClientDetails) o;
        return Objects.equals(appId, that.appId) &&
            Objects.equals(resourceIds, that.resourceIds) &&
            Objects.equals(appSecret, that.appSecret) &&
            Objects.equals(scope, that.scope) &&
            Objects.equals(grantTypes, that.grantTypes) &&
            Objects.equals(redirectUrl, that.redirectUrl) &&
            Objects.equals(authorities, that.authorities) &&
            Objects.equals(accessTokenValidity, that.accessTokenValidity) &&
            Objects.equals(refreshTokenValidity, that.refreshTokenValidity) &&
            Objects.equals(additionalInformation, that.additionalInformation) &&
            Objects.equals(autoApproveScopes, that.autoApproveScopes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appId, resourceIds, appSecret, scope, grantTypes, redirectUrl, authorities, accessTokenValidity, refreshTokenValidity, additionalInformation, autoApproveScopes);
    }
}
