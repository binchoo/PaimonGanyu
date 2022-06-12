package org.binchoo.paimonganyu.chatbot.view.uid;

import org.binchoo.paimonganyu.hoyopass.Region;
import org.binchoo.paimonganyu.hoyopass.Uid;
import org.binchoo.paimonganyu.hoyopass.UserHoyopass;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbinchoo
 * @since 2022/06/13
 */
public class UidModelMap extends ModelMap {

    public static class Item {

        private String server, uid, name;
        private int level;
        private boolean gender;

        public Item(Uid uid) {
            this.server = cutOff(uid.getRegion(), 3);
            this.uid = uid.getUidString();
            this.name = uid.getCharacterName();
            this.level = uid.getCharacterLevel();
            this.gender = uid.getIsLumine();
        }

        private String cutOff(Region region, int start) {
            String regionName = region.name();
            return regionName.substring(start).toUpperCase();
        }

        public String getServer() {
            return server;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public boolean isLumine() {
            return gender;
        }
    }

    public UidModelMap(UserHoyopass userHoyopass) {
        List<Uid> uids = userHoyopass.listUids();
        addAttribute("items", uids.stream().map(Item::new)
                        .collect(Collectors.toList()));
    }
}
