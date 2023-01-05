package com.aacoptics.temphumidity.config;

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
@ConfigurationProperties(prefix = "notify")
public class NotifyMemberConfig implements Serializable {

    @JsonProperty("one")
    private String one;

    @JsonProperty("two")
    private String two;

    @JsonProperty("three")
    private String three;

    @JsonProperty("four")
    private String four;

    @JsonProperty("five")
    private String five;

    @JsonProperty("six")
    private String six;

    @JsonProperty("seven")
    private String seven;
}
