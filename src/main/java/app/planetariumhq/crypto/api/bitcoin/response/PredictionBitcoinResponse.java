package app.planetariumhq.crypto.api.bitcoin.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictionBitcoinResponse {
    private int predictionHeight;

    private String predictionDifficulty;

    private double mineAverageTime;

    private double difficultyChangePercent;
}
