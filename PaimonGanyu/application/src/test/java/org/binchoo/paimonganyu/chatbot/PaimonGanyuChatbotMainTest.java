package org.binchoo.paimonganyu.chatbot;

import org.binchoo.paimonganyu.testamazonclients.TestAmazonClientsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : jbinchoo
 * @since : 2022-04-15
 */

// These two config classes should be imported within this order.
// because, 'socialsignin' library depends on amazonDynamoDB bean 'by name' :-(
// That's why we register @Primary testAmazonDynamoDB having alias, before main amazonDynamoDB bean is registered.
// See also, {@link TestAmazonClientsConfig.class}
@SpringBootTest(classes = {TestAmazonClientsConfig.class, PaimonGanyuChatbotMain.class})
class PaimonGanyuChatbotMainTest {

    @Test
    void bootstrap() { }
}