package org.binchoo.paimonganyu.chatbot.configs.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author jbinchoo
 * @since 2022/06/27
 */
@Slf4j
@Configuration
public class TimeZonConfig {

    @Value("${timezone}")
    private String timeZoneId;

    @PostConstruct
    private void setupTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));
        log.info("TimeZone set {}", timeZoneId);
    }
}
