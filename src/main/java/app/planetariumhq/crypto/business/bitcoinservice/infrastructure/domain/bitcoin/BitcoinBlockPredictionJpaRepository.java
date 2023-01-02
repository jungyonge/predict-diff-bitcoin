package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;


import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPrediction;
import org.springframework.data.repository.CrudRepository;

public interface BitcoinBlockPredictionJpaRepository extends CrudRepository<BitcoinBlockPrediction, Long> {

    BitcoinBlockPrediction findTop1ByOrderByPredictionHeightDesc();
}
