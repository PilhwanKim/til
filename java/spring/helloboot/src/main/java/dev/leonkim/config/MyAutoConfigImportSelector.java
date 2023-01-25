package dev.leonkim.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                "dev.leonkim.config.autoconfig.DispatcherServletConfig",
                "dev.leonkim.config.autoconfig.TomcatWebServerConfig"
        };
    }
}
