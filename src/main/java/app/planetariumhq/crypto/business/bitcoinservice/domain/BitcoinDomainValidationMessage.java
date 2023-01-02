package app.planetariumhq.crypto.business.bitcoinservice.domain;

import app.planetariumhq.crypto.support.domain.ExplainableMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BitcoinDomainValidationMessage implements ExplainableMessage {

    INVALID_OVER_HEIGHT(1_0001, "마지막 채굴된 height 보다 파라미터가 높습니다."),
    INVALID_HEIGHT(1_0002, "블록 height은 음수 일 수 없습니다."),
    INVALID_PREDICTION_NULL(1_0003, "예측되는 블록 정보가 없습니다. 관리자에게 문의해주세요."),

    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    BitcoinDomainValidationMessage(int code, String message) {
        this.code = code;
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public int getStatus() {
        return status.value();
    }
}
