package app.planetariumhq.crypto.api.bitcoin;

import app.planetariumhq.crypto.api.bitcoin.response.BitcoinBlockDto;
import app.planetariumhq.crypto.api.bitcoin.response.PredictionBitcoinResponse;
import app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin.GetBitcoinBlockHandler;
import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bitcoin")
public class BitcoinController {

    private final GetBitcoinBlockHandler getBitcoinBlockHandler;

    private final ModelMapper modelMapper;


    public BitcoinController(GetBitcoinBlockHandler getBitcoinBlockHandler, ModelMapper modelMapper) {
        this.getBitcoinBlockHandler = getBitcoinBlockHandler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<BitcoinBlockDto>> getBitcoinBlockList(@RequestParam int height){
        var results = getBitcoinBlockHandler.getBitcoinBlockList(height);
        Type listType = new TypeToken<List<BitcoinBlockDto>>() {}.getType();
        List<BitcoinBlockDto> list = modelMapper.map(results, listType);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/predict-difficulty")
    public ResponseEntity<PredictionBitcoinResponse> getPredictDifficulty(){
        var results = getBitcoinBlockHandler.getPredictDifficulty();

        return ResponseEntity.ok(results);
    }
}
