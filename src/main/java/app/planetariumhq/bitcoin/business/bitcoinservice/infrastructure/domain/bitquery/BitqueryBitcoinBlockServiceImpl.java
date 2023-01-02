package app.planetariumhq.bitcoin.business.bitcoinservice.infrastructure.domain.bitquery;

import app.planetariumhq.bitcoin.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.bitcoin.business.bitcoinservice.domain.bitcoin.BitcoinBlockService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BitqueryBitcoinBlockServiceImpl implements BitcoinBlockService {

    private final BitqueryService bitqueryService;

    public BitqueryBitcoinBlockServiceImpl(BitqueryService bitqueryService) {
        this.bitqueryService = bitqueryService;
    }

    @Override
    public BitcoinBlock getBitcoinBlock(int height) {
        bitqueryService.getBitcoinBlock(height);
        return null;
    }

    @Override
    public List<BitcoinBlock> getBitcoinBlockList(int startHeight, int lastHeight) {
        bitqueryService.getBitcoinBlockList(startHeight, lastHeight);
        return null;
    }
}
