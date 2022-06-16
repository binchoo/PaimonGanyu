package org.binchoo.paimonganyu.hoyoapi.support;

import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.hoyoapi.DataSwitchConfigurer;
import org.binchoo.paimonganyu.hoyoapi.HoyolabGameRecordApi;
import org.binchoo.paimonganyu.hoyoapi.pojo.LtuidLtoken;
import org.binchoo.paimonganyu.hoyoapi.pojo.enums.DataSwitch;

/**
 * @author jbinchoo
 * @since 2022/06/16
 */
@Slf4j
public class DefaultDataSwitchConfigurer implements DataSwitchConfigurer {

    private final HoyolabGameRecordApi gameRecordApi;

    public DefaultDataSwitchConfigurer(HoyolabGameRecordApi gameRecordApi) {
        this.gameRecordApi = gameRecordApi;
    }

    @Override
    public boolean turnOnSwitches(LtuidLtoken ltuidLtoken) {
        return changeSwitches(ltuidLtoken, true);
    }

    @Override
    public boolean turnOffSwitches(LtuidLtoken ltuidLtoken) {
        return changeSwitches(ltuidLtoken, false);
    }

    private boolean changeSwitches(LtuidLtoken ltuidLtoken, boolean turnOn) {
        for (DataSwitch dataSwitch : DataSwitch.values()) {
            try {
                gameRecordApi.changeDataSwitch(ltuidLtoken, dataSwitch, turnOn);
            } catch (Exception e) {
                log.error(String.format(
                        "Faild to turn %s data switches", (turnOn)? "on" : "off"), e);
                return false;
            }
        }
        return true;
    }
}
