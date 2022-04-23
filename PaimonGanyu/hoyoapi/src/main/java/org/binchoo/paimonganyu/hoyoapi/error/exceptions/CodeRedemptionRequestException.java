package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

import org.binchoo.paimonganyu.hoyoapi.error.Retcode;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;

@Retcode(codes = {-2021, -2018, -2017, -2003, -2001, -1073, -502})
public class CodeRedemptionRequestException extends RetcodeException {
}