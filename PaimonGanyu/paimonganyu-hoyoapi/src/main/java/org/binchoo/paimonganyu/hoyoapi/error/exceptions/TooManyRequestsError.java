package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

import org.binchoo.paimonganyu.hoyoapi.error.Retcode;
import org.binchoo.paimonganyu.hoyoapi.error.RetcodeException;

@Retcode(codes = 10101)
public class TooManyRequestsError extends RetcodeException {
}