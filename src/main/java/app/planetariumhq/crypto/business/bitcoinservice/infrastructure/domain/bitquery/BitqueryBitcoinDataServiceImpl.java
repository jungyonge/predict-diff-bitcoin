package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitquery;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockData;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinDataService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BitqueryBitcoinDataServiceImpl implements BitcoinDataService {

    private final BitqueryService bitqueryService;

    public BitqueryBitcoinDataServiceImpl(BitqueryService bitqueryService) {
        this.bitqueryService = bitqueryService;
    }

    @Override
    public BitcoinBlockData getBitcoinBlock(int height) {
        return bitqueryService.getBitcoinBlock(height);
    }

    @Override
    public List<BitcoinBlockData> getBitcoinBlockList(int startHeight) {
        return bitqueryService.getBitcoinBlockList(startHeight);
    }
}
