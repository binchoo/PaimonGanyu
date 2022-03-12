package org.binchoo.paimonganyu.hoyopass.service;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyoapi.HoyolabAccountApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.entity.Uid;
import org.binchoo.paimonganyu.hoyopass.repository.HoyopassRepository;
import org.binchoo.paimonganyu.hoyopass.repository.UidRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HoyopassServiceImpl implements HoyopassService {

    private final HoyopassSecurityService securityService;
    private final HoyopassRepository hoyopassRepository;
    private final UidRepository uidRepository; // TODO: Use UidService instead.
    private final HoyolabAccountApi apiClient;

    @Override
    public Hoyopass createHoyopass(String userId, String secureHoyopass) {
        LtuidLtoken decoded = securityService.decodeSecureHoyopass(secureHoyopass);
        return hoyopassRepository.save(uidListFilled(
                Hoyopass.builder()
                        .userId(userId)
                        .ltuid(decoded.getLtuid())
                        .ltoken(decoded.getLtoken())
                        .build()));
    }

    @Override
    public Hoyopass createHoyopass(String userId, String ltuid, String ltoken) {
        return hoyopassRepository.save(uidListFilled(
                Hoyopass.builder()
                        .userId(userId)
                        .ltuid(ltuid)
                        .ltoken(ltoken)
                        .build()));
    }

    private Hoyopass uidListFilled(Hoyopass hoyopass) {
        return null;
//        return hoyopass.toBuilder()
//                .uidList(apiClient.getUserGameRoles(HoyopassUtils.ltuidLtoken(hoyopass)).stream()
//                        .map(UserGameRole::getGameUid)
//                        .collect(Collectors.toList()))
//                .build();
    }

    @Override
    public List<Hoyopass> getHoyopassList(String userId) {
        return hoyopassRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Uid> getUidList(String userId, String hoyopassId) {
        Hoyopass hoyopass = this.getHoyopassOfUser(userId, hoyopassId);

        List<Uid> result = new ArrayList<>();
        uidRepository.findAllById(hoyopass.getUidList())
                .forEach(result::add);

        return result;
    }

    @Override
    public void deleteHoyopass(String userId, String hoyopassId) {
        Hoyopass hoyopass = this.getHoyopassOfUser(userId, hoyopassId);
        List<Uid> uidEntityList = this.getUidList(userId, hoyopassId);
        hoyopassRepository.delete(hoyopass);
        uidEntityList.forEach(uid-> uidRepository.delete(uid));
    }

    private Hoyopass getHoyopassOfUser(String userId, String hoyopassId) {
        Hoyopass hoyopass = hoyopassRepository.findById(hoyopassId)
                .orElseThrow(IllegalArgumentException::new);

        if (!userId.equals(hoyopassId))
            throw new IllegalStateException(
                    String.format("%s doesn't own $%s", userId, hoyopassId));

        return hoyopass;
    }
}
