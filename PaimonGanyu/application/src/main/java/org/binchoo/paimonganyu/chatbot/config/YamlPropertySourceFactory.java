package org.binchoo.paimonganyu.chatbot.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author jbinchoo
 * @since 2022/06/13
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean propertyFactory = new YamlPropertiesFactoryBean();
        propertyFactory.setResources(resource.getResource());
        Properties properties = propertyFactory.getObject();
        return new PropertiesPropertySource(resource.getResource().getFilename(), properties);
    }
}
