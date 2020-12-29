package com.example.springbootexample.dynamicLoadConfig;

import com.example.springbootexample.dynamicLoading2.JWReidsCOnfiguration;
import com.example.springbootexample.dynamicLoading1.JWMybatisConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class JWDefineImportSelector implements ImportSelector {
    /**
     * 告诉spring引擎我们的配置类在哪里
     * @param annotationMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
            JWMybatisConfiguration.class.getName() ,
                    JWReidsCOnfiguration.class.getName()
        };
    }
}
