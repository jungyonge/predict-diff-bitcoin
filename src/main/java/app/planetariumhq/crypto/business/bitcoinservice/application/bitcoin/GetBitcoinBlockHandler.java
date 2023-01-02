package app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetBitcoinBlockHandler {

    private final BitcoinDataService bitqueryBitcoinDataService;

    public GetBitcoinBlockHandler(
            @Qualifier("bitqueryBitcoinDataServiceImpl") BitcoinDataService bitcoinDataService) {
        this.bitqueryBitcoinDataService = bitcoinDataService;
    }

}
