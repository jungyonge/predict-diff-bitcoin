package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import org.springframework.data.repository.CrudRepository;

public interface BitcoinBlockJpaRepository extends CrudRepository<BitcoinBlock, Long> {

}
