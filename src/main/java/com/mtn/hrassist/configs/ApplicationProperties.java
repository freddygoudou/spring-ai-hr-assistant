package com.mtn.hrassist.configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "apps")
public class ApplicationProperties {
    private String fileUploadDir;
}
