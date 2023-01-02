package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class BitcoinBlockBatchJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private int lastBitcoinHeight;

    public void updateHeight(int lastBitcoinHeight){
        this.setLastBitcoinHeight(lastBitcoinHeight);
    }

    private void setLastBitcoinHeight(int lastBitcoinHeight) {
        this.lastBitcoinHeight = lastBitcoinHeight;
    }
}
