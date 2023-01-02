package app.planetariumhq.crypto.support.domain;

public class DomainValidationException extends RuntimeException {

    private int code;
    private int httpStatus;

    public DomainValidationException(ExplainableMessage explainableMessage) {
        super(explainableMessage.getMessage());
        this.code = explainableMessage.getCode();
        this.httpStatus = explainableMessage.getStatus();
    }
    public int getCode() {
        return this.code;
    }
    public int getHttpStatus() {
        return this.httpStatus;
    }

}
