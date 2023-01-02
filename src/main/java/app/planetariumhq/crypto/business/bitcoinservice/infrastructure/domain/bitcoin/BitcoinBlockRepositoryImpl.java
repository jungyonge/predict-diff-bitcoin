package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class BitcoinBlockRepositoryImpl implements BitcoinBlockRepository {

    private final BitcoinBlockJpaRepository bitcoinBlockJpaRepository;

    @Override
    public BitcoinBlock save(BitcoinBlock bitcoinBlock) {
        return bitcoinBlockJpaRepository.save(bitcoinBlock);
    }

    @Override
    public Iterable<BitcoinBlock> saveAll(List<BitcoinBlock> bitcoinBlocks) {
        return bitcoinBlockJpaRepository.saveAll(bitcoinBlocks);
    }

    @Override
    public List<BitcoinBlock> getBitcoinBlockListByHeight(int height) {
        return bitcoinBlockJpaRepository.findAllByHeightGreaterThanEqual(height);
    }

    @Override
    public BitcoinBlock getBitcoinBlockByHeight(int height) {
        return bitcoinBlockJpaRepository.findByHeight(height);
    }

    @Override
    public BitcoinBlock getLastBitcoinBlock() {
        return bitcoinBlockJpaRepository.findTop1ByOrderByMinedDesc();
    }
}
