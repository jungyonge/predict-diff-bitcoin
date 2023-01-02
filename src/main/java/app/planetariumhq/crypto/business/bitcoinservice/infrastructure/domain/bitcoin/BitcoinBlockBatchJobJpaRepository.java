package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJob;
import org.springframework.data.repository.CrudRepository;

public interface BitcoinBlockBatchJobJpaRepository extends CrudRepository<BitcoinBlockBatchJob, Long> {

    BitcoinBlockBatchJob getBitcoinBlockBatchJobById(long id);
}
