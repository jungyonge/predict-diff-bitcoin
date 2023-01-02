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

    private LocalDateTime predictionChangeDate;

    private String predictionDifficulty;

    private String presentDifficulty;

    private double mineAverageTime;

    private double difficultyChangePercent;

    private LocalDateTime created;

    private LocalDateTime updated;

    private BitcoinBlockPrediction(int predictionHeight, String predictionDifficulty,
            double mineAverageTime, double difficultyChangePercent, LocalDateTime predictionChangeDate,
            String presentDifficulty) {
        this.setPredictionHeight(predictionHeight);
        this.setPredictionDifficulty(predictionDifficulty);
        this.setMineAverageTime(mineAverageTime);
        this.setDifficultyChangePercent(difficultyChangePercent);
        this.setPredictionChangeDate(predictionChangeDate);
        this.setPresentDifficulty(presentDifficulty);
        this.setCreated(LocalDateTime.now());
    }

    public static BitcoinBlockPrediction create(int predictionHeight, String predictionDifficulty,
            double mineAverageTime, double difficultyChangePercent, LocalDateTime predictionChangeDate,
            String presentDifficulty) {

        return new BitcoinBlockPrediction(predictionHeight, predictionDifficulty, mineAverageTime,
                difficultyChangePercent, predictionChangeDate, presentDifficulty);
    }

    public void updatePrediction(String predictionDifficulty, double mineAverageTime,
            double difficultyChangePercent, LocalDateTime predictionChangeDate, String presentDifficulty) {
        this.setPredictionDifficulty(predictionDifficulty);
        this.setMineAverageTime(mineAverageTime);
        this.setDifficultyChangePercent(difficultyChangePercent);
        this.setPredictionChangeDate(predictionChangeDate);
        this.setPresentDifficulty(presentDifficulty);
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

    private void setPredictionChangeDate(LocalDateTime predictionChangeDate) {
        this.predictionChangeDate = predictionChangeDate;
    }

    private void setPresentDifficulty(String presentDifficulty) {
        this.presentDifficulty = presentDifficulty;
    }

    private void setCreated(LocalDateTime created) {
        this.created = created;
    }

    private void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
