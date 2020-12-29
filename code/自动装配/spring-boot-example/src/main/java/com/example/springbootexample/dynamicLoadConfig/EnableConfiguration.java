package com.example.springbootexample.dynamicLoadConfig;


import com.example.springbootexample.dynamicLoadConfig.JWDefineImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JWDefineImportSelector.class)
public @interface EnableConfiguration {
}
