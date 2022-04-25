package org.binchoo.paimonganyu.awsutils.support;

@FunctionalInterface
public interface AwsEventWrapperMappingConfigurer {

    void configure(AwsEventWrappingManual mappingManual);
}
