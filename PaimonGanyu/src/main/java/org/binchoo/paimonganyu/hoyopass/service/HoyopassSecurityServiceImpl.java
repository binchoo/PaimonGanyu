package org.binchoo.paimonganyu.hoyopass.service;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.springframework.stereotype.Service;

@Service
public class HoyopassSecurityServiceImpl implements HoyopassSecurityService {
    @Override
    public LtuidLtoken decodeSecureHoyopass(String secureHoyopass) {
        return null;
    }
}