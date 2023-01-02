package app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin;

import app.planetariumhq.crypto.api.bitcoin.response.PredictionBitcoinResponse;
import app.planetariumhq.crypto.business.bitcoinservice.domain.BitcoinDomainValidationMessage;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPrediction;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPredictionRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import app.planetariumhq.crypto.support.domain.DomainValidationException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<BitcoinBlock> getBitcoinBlockList(int height) {
        BitcoinBlock bitcoinBlock = bitcoinBlockRepository.getLastBitcoinBlock();
        if(height > bitcoinBlock.getHeight()){
            throw new DomainValidationException(BitcoinDomainValidationMessage.INVALID_OVER_HEIGHT);
        }
        if(height < 0){
            throw new DomainValidationException(BitcoinDomainValidationMessage.INVALID_HEIGHT);
        }
        return bitcoinBlockRepository.getBitcoinBlockListByHeight(height);
    }
    @Transactional(readOnly = true)
    public PredictionBitcoinResponse getPredictDifficulty() {
        BitcoinBlockPrediction bitcoinBlockPrediction = bitcoinBlockPredictionRepository.getBitcoinBlockPrediction();
        if(bitcoinBlockPrediction == null){
            throw new DomainValidationException(BitcoinDomainValidationMessage.INVALID_PREDICTION_NULL);
        }
        return modelMapper.map(bitcoinBlockPrediction, PredictionBitcoinResponse.class);
    }


}
