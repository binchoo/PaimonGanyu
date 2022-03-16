package org.binchoo.paimonganyu.hoyopass.app;

import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassRepositoryPort;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassSearchPort;
import org.binchoo.paimonganyu.hoyopass.domain.driving.HoyopassRegistryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class HoyopassRegistryApp implements HoyopassRegistryPort {

    private final HoyopassSearchPort hoyopassSearchPort;
    private final HoyopassRepositoryPort hoyopassRepositoryPort;

    @Override
    public boolean registerSecureHoyopass(String userId, String secureHoyopass) {
        // TODO: implement this method
        return false;
    }

    @Override
    public boolean registerHoyopass(String botUserId, String ltuid, String ltoken) {
        final Hoyopass newHoyopass = Hoyopass.builder()
                .botUserId(botUserId).ltuid(ltuid).ltoken(ltoken).build();
        final Hoyopass filled
                = hoyopassSearchPort.fillUids(newHoyopass);
        final Hoyopass saved
                = hoyopassRepositoryPort.save(filled);
        return Objects.nonNull(saved);
    }

    @Override
    public List<Hoyopass> listHoyopasses(String botUserId) {
        return hoyopassRepositoryPort.findByBotUserId(botUserId)
                .stream().sorted().collect(Collectors.toList());
    }

    @Override
    public List<Uid> listUids(String botUserId) {
        final List<Hoyopass> userHoyopasses = this.listHoyopasses(botUserId);
        return userHoyopasses.stream()
                .map(hoyopass -> hoyopass.getUids().stream())
                .reduce(Stream::concat).orElseThrow(()-> new IllegalStateException("Uid reduction failed"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Uid> listUids(String botUserId, int order) {
        final List<Hoyopass> userHoyopasses = this.listHoyopasses(botUserId);

        if (order >= userHoyopasses.size())
            throw new IllegalArgumentException(
                    String.format("Designated hoyopass %d could not be found.", order));

        return userHoyopasses.get(order).getUids();
    }

    @Override
    public void deleteHoyopass(String botUserId, int order) {
        final List<Hoyopass> userHoyopasses = this.listHoyopasses(botUserId);

        if (order >= userHoyopasses.size())
            throw new IllegalArgumentException(
                    String.format("Designated hoyopass %d could not be found.", order));

        hoyopassRepositoryPort.delete(userHoyopasses.get(order));
    }
}
