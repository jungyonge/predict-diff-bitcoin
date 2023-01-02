package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_predictionHeight", columnList = "predictionHeight")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_predictionHeight",
                        columnNames = {"predictionHeight"}
                )
        }
)
@Getter
@NoArgsConstructor
public class BitcoinBlockPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    private int predictionHeight;

    private String predictionDifficulty;

    private double mineAverageTime;

    private double difficultyChangePercent;

    private LocalDateTime created;

    private LocalDateTime updated;

    private BitcoinBlockPrediction(int predictionHeight, String predictionDifficulty, double mineAverageTime, double difficultyChangePercent){
        this.setPredictionHeight(predictionHeight);
        this.setPredictionDifficulty(predictionDifficulty);
        this.setMineAverageTime(mineAverageTime);
        this.setDifficultyChangePercent(difficultyChangePercent);
        this.setCreated(LocalDateTime.now());
    }

    public static BitcoinBlockPrediction create(int predictionHeight, String predictionDifficulty, double mineAverageTime, double difficultyChangePercent){

        return new BitcoinBlockPrediction(predictionHeight, predictionDifficulty, mineAverageTime, difficultyChangePercent);
    }

    public void updatePrediction(String predictionDifficulty, double mineAverageTime, double difficultyChangePercent){
        this.setPredictionDifficulty(predictionDifficulty);
        this.setMineAverageTime(mineAverageTime);
        this.setDifficultyChangePercent(difficultyChangePercent);
        this.setUpdated(LocalDateTime.now());
    }


    private void setPredictionHeight(int predictionHeight) {
        this.predictionHeight = predictionHeight;
    }

    private void setPredictionDifficulty(String predictionDifficulty) {
        this.predictionDifficulty = predictionDifficulty;
    }

    private void setMineAverageTime(double mineAverageTime) {
        this.mineAverageTime = mineAverageTime;
    }

    private void setDifficultyChangePercent(double difficultyChangePercent) {
        this.difficultyChangePercent = difficultyChangePercent;
    }

    private void setCreated(LocalDateTime created) {
        this.created = created;
    }

    private void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
