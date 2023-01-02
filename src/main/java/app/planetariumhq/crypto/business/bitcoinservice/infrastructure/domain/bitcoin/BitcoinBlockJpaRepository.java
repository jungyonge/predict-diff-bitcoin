package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BitcoinBlockJpaRepository extends CrudRepository<BitcoinBlock, Long> {

    List<BitcoinBlock> findAllByHeightGreaterThanEqual(int height);

    BitcoinBlock findByHeight(int height);

    BitcoinBlock findTop1ByOrderByMinedDesc();

}
