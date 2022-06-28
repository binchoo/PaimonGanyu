package org.binchoo.paimonganyu.chatbot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jbinchoo
 * @since 2022/06/24
 */
@Controller
public class HealthCheckController {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
