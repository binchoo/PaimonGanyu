package org.binchoo.paimonganyu.dailycheck;

public class DailyCheckRequestResult {

    private String message;
    private Throwable error;
    private boolean hasFailed;
    private boolean isDuplicated;

    public DailyCheckRequestResult() {
        message = "";
        error = null;
        hasFailed = false;
        isDuplicated = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean hasFailed() {
        return hasFailed;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Exception e) {
        hasFailed = true;
        error = e;
    }

    public boolean isDuplicated() {
        return isDuplicated;
    }

    public void setDuplicated(boolean isDuplicated) {
        this.isDuplicated = isDuplicated;
    }
}
