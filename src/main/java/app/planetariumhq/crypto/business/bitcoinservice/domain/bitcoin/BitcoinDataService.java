package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

import java.util.List;

public interface BitcoinDataService {

    BitcoinBlockData getBitcoinBlock(int height);

    List<BitcoinBlockData> getBitcoinBlockList(int startHeight);

}
