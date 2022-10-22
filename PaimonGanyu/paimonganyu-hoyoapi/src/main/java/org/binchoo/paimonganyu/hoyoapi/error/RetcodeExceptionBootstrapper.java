package org.binchoo.paimonganyu.hoyoapi.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

class RetcodeExceptionBootstrapper {

    private static final Logger logger = LoggerFactory.getLogger(RetcodeExceptionBootstrapper.class);

    protected static final String PACKAGE_NAME = RetcodeExceptionBootstrapper.class.getPackage().getName();
    protected static final String EXCECPTION_PACKAGE_NAME = PACKAGE_NAME + ".exceptions";

    private static final ClassPathScanningCandidateComponentProvider provider
            = new ClassPathScanningCandidateComponentProvider(false);

    private static final RetcodeExceptionMappings mappings = RetcodeExceptionMappings.getInstance();

    private RetcodeExceptionBootstrapper() {}
    
    protected static void start() {
        initComponentProvider();
        initRetcodeExceptionMappings();
    }

    private static void initComponentProvider() {
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
    }

    private static void initRetcodeExceptionMappings() {
        for (Class<RetcodeException> clazz : scanExceptionPackage()) {
            Retcode annotation = clazz.getAnnotation(Retcode.class);
            if (annotation != null) {
                for (int retcode : annotation.codes())
                    mappings.addMapping(retcode, clazz);
            }
        }
    }

    private static List<Class<RetcodeException>> scanExceptionPackage() {
        Set<BeanDefinition> beanDefinitions =
                provider.findCandidateComponents(EXCECPTION_PACKAGE_NAME);

        List<Class<RetcodeException>> exceptions = new LinkedList<>();
        for (BeanDefinition bdf : beanDefinitions) {
            try {
                Class<RetcodeException> retcodeExceptionClass
                        = (Class<RetcodeException>) Class.forName(bdf.getBeanClassName());
                exceptions.add(retcodeExceptionClass);
            } catch (ClassNotFoundException e) {
                logger.warn("Error loading a retcode exception class.", e);
            }
        }
        return exceptions;
    }
}
