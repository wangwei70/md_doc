package com.better.config;


import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Data
@Component
@ConfigurationProperties(prefix = "help.doc")
public class DocProperty implements InitializingBean {

    private String classpathDir;
    private Boolean enableReload;
    private String accurateDir;


    private static final String DEFALT_CLASSPATH_DIR = "classpath:doc";
    private static final Boolean DEFAULT_ENABLE_RELOAD = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        enableReload = enableReload == null ? DEFAULT_ENABLE_RELOAD : enableReload;
        classpathDir = classpathDir == null ? DEFALT_CLASSPATH_DIR : classpathDir;
        accurateDir = accurateDir == null ? ResourceUtils.getFile(classpathDir).getPath() : accurateDir;
    }
}
