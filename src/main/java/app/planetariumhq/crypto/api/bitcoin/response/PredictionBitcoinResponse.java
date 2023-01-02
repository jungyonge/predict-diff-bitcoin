package app.planetariumhq.crypto.api.bitcoin.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictionBitcoinResponse {

    private int predictionHeight;

    private String predictionDifficulty;

    private String presentDifficulty;

    private double mineAverageTime;

    private double difficultyChangePercent;

    private LocalDateTime predictionChangeDate;
}
