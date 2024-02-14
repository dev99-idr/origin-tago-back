package com.tagoBackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// ** Collection of setting variables to be used in Checkie **
@Component
@ConfigurationProperties(prefix = "idrframework")
@Data
public class IdrProperties {
    // Server IP
    private String serverIp;
    // Server Port
    private String ServerPort;

}
