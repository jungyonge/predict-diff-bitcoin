package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJob;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJobRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BitcoinBlockBatchJobRepositoryImpl implements BitcoinBlockBatchJobRepository {

    private final BitcoinBlockBatchJobJpaRepository bitcoinBlockBatchJobJpaRepository;

    public BitcoinBlockBatchJobRepositoryImpl(
            BitcoinBlockBatchJobJpaRepository bitcoinBlockBatchJobJpaRepository) {
        this.bitcoinBlockBatchJobJpaRepository = bitcoinBlockBatchJobJpaRepository;
    }

    @Override
    public BitcoinBlockBatchJob save(BitcoinBlockBatchJob bitcoinBlockBatchJob) {
        return bitcoinBlockBatchJobJpaRepository.save(bitcoinBlockBatchJob);
    }

    @Override
    public BitcoinBlockBatchJob getBitcoinBlockBatchJob() {
        return bitcoinBlockBatchJobJpaRepository.getBitcoinBlockBatchJobById(1L);
    }
}
