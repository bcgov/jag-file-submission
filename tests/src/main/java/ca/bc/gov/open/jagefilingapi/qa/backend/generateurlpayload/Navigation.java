package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class Navigation {

    private Success success;
    private Error error;
    private Cancel cancel;

    public Navigation(Success success, Error error, Cancel cancel) {
        this.success = success;
        this.error = error;
        this.cancel = cancel;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Cancel getCancel() {
        return cancel;
    }

    public void setCancel(Cancel cancel) {
        this.cancel = cancel;
    }
}
