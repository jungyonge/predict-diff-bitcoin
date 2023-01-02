package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

public interface BitcoinBlockPredictionRepository {

    BitcoinBlockPrediction save(BitcoinBlockPrediction bitcoinBlockPrediction);

    BitcoinBlockPrediction getBitcoinBlockPrediction();

}
