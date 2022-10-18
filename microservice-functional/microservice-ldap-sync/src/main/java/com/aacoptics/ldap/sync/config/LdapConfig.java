package com.aacoptics.ldap.sync.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "ldap-info")
public class LdapConfig implements Serializable {

    private String domainName;

    private String domainRoot;

    private String mailDomainRoot;

    private String adminName;

    private String adminPass;

    private String domainUrl;

    private String apiUser;

    private String apiPassword;

    private String apiUrl;
}
