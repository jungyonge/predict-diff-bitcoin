package app.planetariumhq.bitcoin.business.bitcoinservice.domain.bitcoin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitcoinBlock {

    private String height;
    private String blockHash;
    private String difficulty;
    private HashMap<String, Object> timestamp;

}
