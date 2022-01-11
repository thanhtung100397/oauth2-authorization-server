package com.spring.oauth2_authorization_server.modules.auth.models.entities;

import com.spring.oauth2_authorization_server.base.json_converter.JsonListConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client_application")
public class ClientApplication {
    @Id
    @Column(name = "client_id")
    private String clientID;
    @Column(name = "secret")
    private String secret;
    @Column(name = "authorized_grant_types")
    @Convert(converter = JsonListConverter.class)
    private List<String> authorizedGrantTypes;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "client_resource",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "resource_id")
    )
    private Set<ResourceServer> resourceServers = new HashSet<>();

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public Set<ResourceServer> getResourceServers() {
        return resourceServers;
    }

    public void setResourceServers(Set<ResourceServer> resourceServers) {
        if (resourceServers != null) {
            this.resourceServers = resourceServers;
        }
    }

    @Override
    public int hashCode() {
        return this.clientID == null ? 0 : this.clientID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClientApplication) {
            if (this.clientID == null) {
                return ((ClientApplication) obj).clientID == null;
            } else {
                return this.clientID.equals(((ClientApplication) obj).clientID);
            }
        }
        return false;
    }
}
