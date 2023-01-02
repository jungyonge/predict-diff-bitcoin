package app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin;

import app.planetariumhq.crypto.api.bitcoin.response.PredictionBitcoinResponse;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPrediction;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPredictionRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GetBitcoinBlockHandler {

    private final BitcoinBlockRepository bitcoinBlockRepository;
    private final BitcoinBlockPredictionRepository bitcoinBlockPredictionRepository;
    private final ModelMapper modelMapper;


    public GetBitcoinBlockHandler(BitcoinBlockRepository bitcoinBlockRepository,
            BitcoinBlockPredictionRepository bitcoinBlockPredictionRepository,
            ModelMapper modelMapper) {
        this.bitcoinBlockRepository = bitcoinBlockRepository;
        this.bitcoinBlockPredictionRepository = bitcoinBlockPredictionRepository;
        this.modelMapper = modelMapper;
    }

    public List<BitcoinBlock> getBitcoinBlockList(int height){
        return bitcoinBlockRepository.getBitcoinBlockListByHeight(height);
    }

    public PredictionBitcoinResponse getPredictDifficulty(){
        BitcoinBlockPrediction bitcoinBlockPrediction = bitcoinBlockPredictionRepository.getBitcoinBlockPrediction();
        return modelMapper.map(bitcoinBlockPrediction, PredictionBitcoinResponse.class);
    }


}
