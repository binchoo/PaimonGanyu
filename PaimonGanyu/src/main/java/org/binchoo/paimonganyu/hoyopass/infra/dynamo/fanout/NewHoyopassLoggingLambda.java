package org.binchoo.paimonganyu.hoyopass.infra.dynamo.fanout;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewHoyopassLoggingLambda {

    public void handler(SNSEvent snsEvent) {
        snsEvent.getRecords().forEach(snsRecord -> {
            log.info(snsRecord.getSNS().getMessage());
        });
    }
}
