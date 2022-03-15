package org.binchoo.paimonganyu.hoyoapi.error.exceptions;

import org.binchoo.paimonganyu.hoyoapi.error.Retcode;

@Retcode(codes = {-100, -1071, 10001, 10103})
public class NotLoggedInError extends RetcodeException {
}