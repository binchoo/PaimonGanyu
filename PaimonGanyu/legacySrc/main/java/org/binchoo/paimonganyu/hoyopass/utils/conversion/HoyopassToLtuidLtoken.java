package org.binchoo.paimonganyu.hoyopass.utils.conversion;

import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HoyopassToLtuidLtoken implements Converter<Hoyopass, LtuidLtoken> {

    @Override
    public LtuidLtoken convert(Hoyopass source) {
        return new LtuidLtoken(source.getLtuid(), source.getLtoken());
    }
}
