package com.spring.oauth2_authorization_server.modules.auth.models.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resource_server")
public class ResourceServer {
    public static final String RESOURCE_ID = "resourceID";

    @Id
    @Column(name = "resource_id")
    private String resourceID;
    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "resourceServers",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ClientApplication> clientApplications = new HashSet<>();

    public ResourceServer() {
    }

    public ResourceServer(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public Set<ClientApplication> getClientApplications() {
        return clientApplications;
    }

    public void setClientApplications(Set<ClientApplication> clientApplications) {
        this.clientApplications = clientApplications;
    }

    @Override
    public int hashCode() {
        return this.resourceID == null ? 0 : this.resourceID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceServer) {
            if (this.resourceID == null) {
                return ((ResourceServer) obj).resourceID == null;
            } else {
                return this.resourceID.equals(((ResourceServer) obj).resourceID);
            }
        }
        return false;
    }
}
