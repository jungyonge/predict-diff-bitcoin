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
                @Index(name = "idx_height", columnList = "height")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_height",
                        columnNames = {"height"}
                )
        }
)
@Getter
@NoArgsConstructor
public class BitcoinBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private int height;
    private String blockHash;
    private double difficulty;

    private int transactionCount;
    private LocalDateTime mined;
    private LocalDateTime created;

    private BitcoinBlock(int height, String blockHash, double difficulty, LocalDateTime mined, int transactionCount) {
        this.setHeight(height);
        this.setBlockHash(blockHash);
        this.setDifficulty(difficulty);
        this.setMined(mined);
        this.setTransactionCount(transactionCount);
        this.setCreated(LocalDateTime.now());
    }

    public static BitcoinBlock create(int height, String blockHash, double difficulty,
            LocalDateTime mined, int transactionCount) {

        return new BitcoinBlock(height, blockHash, difficulty, mined, transactionCount);
    }

    private void setHeight(int height) {
        this.height = height;
    }

    private void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    private void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    private void setMined(LocalDateTime mined) {
        this.mined = mined;
    }

    private void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    private void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
