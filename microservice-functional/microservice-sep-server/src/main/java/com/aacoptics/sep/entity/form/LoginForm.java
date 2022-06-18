package com.aacoptics.sep.entity.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sep")
public class LoginForm implements Serializable {

    private String username;

    private String password;

    private String domain;
}
