package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPrediction;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPredictionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BitcoinBlockPredictionRepositoryImpl implements BitcoinBlockPredictionRepository {

    private final BitcoinBlockPredictionJpaRepository bitcoinBlockPredictionJpaRepository;

    public BitcoinBlockPredictionRepositoryImpl(
            BitcoinBlockPredictionJpaRepository bitcoinBlockPredictionJpaRepository) {
        this.bitcoinBlockPredictionJpaRepository = bitcoinBlockPredictionJpaRepository;
    }

    @Override
    public BitcoinBlockPrediction save(BitcoinBlockPrediction bitcoinBlockPrediction) {
        return bitcoinBlockPredictionJpaRepository.save(bitcoinBlockPrediction);
    }

    @Override
    public BitcoinBlockPrediction getBitcoinBlockPrediction() {
        return bitcoinBlockPredictionJpaRepository.findTop1ByOrderByPredictionHeightDesc();
    }
}
