package org.binchoo.paimonganyu.config;

import org.binchoo.paimonganyu.PaimonGanyuApp;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(PaimonGanyuApp.class)
@Profile("integ")
public class IntegrationTestConfig {
}
