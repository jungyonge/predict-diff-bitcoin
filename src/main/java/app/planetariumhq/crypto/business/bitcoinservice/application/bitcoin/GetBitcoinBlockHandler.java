package app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetBitcoinBlockHandler {

    private final BitcoinBlockRepository bitcoinBlockRepository;


    public GetBitcoinBlockHandler(BitcoinBlockRepository bitcoinBlockRepository) {
        this.bitcoinBlockRepository = bitcoinBlockRepository;
    }

    public List<BitcoinBlock> getBitcoinBlockList(int height){
        return bitcoinBlockRepository.getBitcoinBlockListByHeight(height);
    }
}
