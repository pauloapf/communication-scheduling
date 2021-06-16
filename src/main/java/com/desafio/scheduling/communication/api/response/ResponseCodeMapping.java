package com.desafio.scheduling.communication.api.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "map")
public class ResponseCodeMapping implements InitializingBean {

    private List<ResponseCode> codeList;
    
    private Map<String, ResponseCode> codeMap;
	
    public ResponseCode get(String extCode) {
        ResponseCode code = null;
        if (codeMap != null) {
            code = codeMap.get(extCode);
        }
        return code;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        codeMap = new HashMap<>();
        if (codeList != null) {
            for (ResponseCode code : codeList) {
                codeMap.put(code.getExtCode(), code);
            }
        }
    }
}
