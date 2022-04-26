package org.binchoo.paimonganyu.awsutils.support;

@FunctionalInterface
public interface AwsEventWrapperFactoryConfigurer {

    void configure(WrappingManual manual);
}
