package app.planetariumhq.crypto.api.bitcoin.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictionBitcoinResponse {

    private int predictionHeight;

    private String presentDifficulty;

    private String predictionDifficulty;

    private double difficultyChangePercent;

    private double mineAverageTime;

    private LocalDateTime predictionChangeDate;
}
