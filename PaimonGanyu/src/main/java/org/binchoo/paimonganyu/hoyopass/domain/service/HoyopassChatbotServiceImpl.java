package org.binchoo.paimonganyu.hoyopass.domain.service;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.inport.HoyopassChatbotService;
import org.binchoo.paimonganyu.hoyopass.domain.outport.HoyopassRepository;
import org.binchoo.paimonganyu.hoyopass.domain.outport.HoyopassSearch;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HoyopassChatbotServiceImpl implements HoyopassChatbotService {

    private final HoyopassSearch hoyopassSearch;
    private final HoyopassRepository hoyopassRepository;

    @Override
    public Hoyopass registerSecureHoyopass(String userId, String secureHoyopass) {
        return null;
    }

    @Override
    public Hoyopass registerHoyopass(String botUserId, String ltuid, String ltoken) {
        return null;
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return null;
    }

    @Override
    public List<Uid> listUids(String userId) {
        return null;
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        return null;
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {

    }
}
