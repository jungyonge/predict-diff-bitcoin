package app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin;

public interface BitcoinBlockBatchJobRepository {

    BitcoinBlockBatchJob save (BitcoinBlockBatchJob bitcoinBlockBatchJob);
    BitcoinBlockBatchJob getBitcoinBlockBatchJob();
}
