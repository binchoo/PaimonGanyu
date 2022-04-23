package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

import org.binchoo.paimonganyu.hoyoapi.error.Retcode;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;

@Retcode(codes = {-2016})
public class CodeRedemptionThrottledException extends RetcodeException {
}