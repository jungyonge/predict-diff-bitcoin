package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

import java.util.List;

public interface BitcoinBlockRepository {

    BitcoinBlock save(BitcoinBlock bitcoinBlock);

    Iterable<BitcoinBlock> saveAll(List<BitcoinBlock> bitcoinBlocks);

    List<BitcoinBlock> getBitcoinBlockListByHeight(int height);

    BitcoinBlock getBitcoinBlockByHeight(int height);

    BitcoinBlock getLastBitcoinBlock();


}
