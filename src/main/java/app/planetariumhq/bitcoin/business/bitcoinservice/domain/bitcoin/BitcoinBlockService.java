package app.planetariumhq.bitcoin.business.bitcoinservice.domain.bitcoin;

import java.util.List;

public interface BitcoinBlockService {

    BitcoinBlock getBitcoinBlock(int height);

    List<BitcoinBlock> getBitcoinBlockList(int startHeight, int lastHeight);

}
