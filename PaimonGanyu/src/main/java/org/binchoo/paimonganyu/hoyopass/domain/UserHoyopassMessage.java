package org.binchoo.paimonganyu.hoyopass.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Simplified version of class for {@UserHoyopass}
 */
@Getter
public class UserHoyopassMessage implements Serializable {

    private String botUserId;
    private String ltuid;
    private String ltoken;
    private String[] uids;

    public UserHoyopassMessage(UserHoyopass userHoyopass) {
        this.botUserId = userHoyopass.getBotUserId();
        for (Hoyopass hoyopass : userHoyopass.getHoyopasses()) {
            List<Uid> uids = hoyopass.getUids();
            this.ltuid = hoyopass.getLtuid();
            this.ltoken = hoyopass.getLtoken();
            this.uids = new String[uids.size()];
            for (int i = 0; i < uids.size(); i++) {
                this.uids[i] = uids.get(i).getUidString();
            }
        }
    }
}
