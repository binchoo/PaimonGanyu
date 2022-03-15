package org.binchoo.paimonganyu.hoyopass.domain.outport;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;

public interface HoyopassSearch {

    Hoyopass fillUids(Hoyopass hoyopass);
    Hoyopass fillIsLumines(Hoyopass hoyopass);
}
