package org.binchoo.paimonganyu.dailycheck;

public class DailyCheckRequestResult {

    private String message;
    private Throwable error;
    private boolean isDuplicated;

    public DailyCheckRequestResult() {
        message = "";
        error = null;
        isDuplicated = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean hasFailed() {
        return error != null;
    }

    public Throwable getError() {
        return error;
    }

    /**
     * @param e NonNull
     */
    public void setError(Throwable e) {
        error = e;
    }

    public boolean isDuplicated() {
        return isDuplicated;
    }

    public void setDuplicated(boolean isDuplicated) {
        this.isDuplicated = isDuplicated;
    }
}
