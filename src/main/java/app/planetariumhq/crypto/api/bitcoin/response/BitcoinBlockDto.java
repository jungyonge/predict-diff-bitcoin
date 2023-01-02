package app.planetariumhq.crypto.api.bitcoin.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BitcoinBlockDto {

    private int height;
    private String blockHash;
    private double difficulty;
    private LocalDateTime mined;
    private int transactionCount;
}
