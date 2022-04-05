package org.binchoo.paimonganyu.hoyopass.infra.fanout;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.Uid;
import org.binchoo.paimonganyu.hoyopass.domain.UserHoyopass;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified version of class for {@UserHoyopass}
 */
public class UserHoyopassMessage implements Serializable {

    private String botUserId;
    private String ltuid;
    private String ltoken;
    private String[] uids;

    public UserHoyopassMessage() { }

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

    public String getBotUserId() {
        return botUserId;
    }

    public String getLtuid() {
        return ltuid;
    }

    public String getLtoken() {
        return ltoken;
    }

    public String[] getUids() {
        return uids;
    }

    @Override
    public String toString() {
        return "UserHoyopassMessage{" +
                "botUserId='" + botUserId + '\'' +
                ", ltuid='" + ltuid + '\'' +
                ", ltoken='" + ltoken + '\'' +
                ", uids=" + Arrays.toString(uids) +
                '}';
    }
}
