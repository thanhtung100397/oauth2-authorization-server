package com.spring.oauth2_authorization_server.modules.oauth2_admin.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "resource_server")
public class OAuth2AdminResourceServer {
    public static final String RESOURCE_ID = "resourceID";

    @Id
    @Column(name = "resource_id")
    private String resourceID;

    public OAuth2AdminResourceServer() {
    }

    public OAuth2AdminResourceServer(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    @Override
    public int hashCode() {
        return this.resourceID == null ? 0 : this.resourceID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OAuth2AdminResourceServer) {
            if (this.resourceID == null) {
                return ((OAuth2AdminResourceServer) obj).resourceID == null;
            } else {
                return this.resourceID.equals(((OAuth2AdminResourceServer) obj).resourceID);
            }
        }
        return false;
    }
}
