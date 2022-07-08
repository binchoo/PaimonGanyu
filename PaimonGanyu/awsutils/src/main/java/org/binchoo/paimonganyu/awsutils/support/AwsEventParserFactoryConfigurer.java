package org.binchoo.paimonganyu.awsutils.support;

@FunctionalInterface
public interface AwsEventParserFactoryConfigurer {

    void configure(WrappingManual manual);
}
