package app.planetariumhq.crypto.batchjob.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin.GetBitcoinBlockHandler;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJob;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJobRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockData;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPrediction;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockPredictionRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinDataService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BitcoinBlockDataJob {

    private final BitcoinBlockBatchJobRepository bitcoinBlockBatchJobRepository;

    private final BitcoinBlockRepository bitcoinBlockRepository;

    private final BitcoinBlockPredictionRepository bitcoinBlockPredictionRepository;
    private final BitcoinDataService bitqueryBitcoinDataService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public BitcoinBlockDataJob(BitcoinBlockBatchJobRepository blockBatchJobRepository,
            BitcoinBlockRepository bitcoinBlockRepository,
            GetBitcoinBlockHandler getBitcoinBlockHandler,
            BitcoinBlockPredictionRepository bitcoinBlockPredictionRepository, @Qualifier("bitqueryBitcoinDataServiceImpl") BitcoinDataService bitqueryBitcoinDataService) {
        this.bitcoinBlockBatchJobRepository = blockBatchJobRepository;
        this.bitcoinBlockRepository = bitcoinBlockRepository;
        this.bitcoinBlockPredictionRepository = bitcoinBlockPredictionRepository;
        this.bitqueryBitcoinDataService = bitqueryBitcoinDataService;
    }

    @Transactional
    @Scheduled(fixedDelay = 60 * 1000)
    public void bitcoinBlockDataBatchJob() {

        int lastHeight = 0;
        BitcoinBlockBatchJob batchJob = bitcoinBlockBatchJobRepository.getBitcoinBlockBatchJob();
        List<BitcoinBlockData> list = bitqueryBitcoinDataService.getBitcoinBlockList(batchJob.getLastBitcoinHeight());

        if (list.size() > 0) {
            Comparator<BitcoinBlockData> cp = (data1, data2) -> {
                int a = data1.getHeight();
                int b = data2.getHeight();
                if (a > b) {
                    return 1;
                } else {
                    return -1;
                }
            };

            list.sort(cp);
            List<BitcoinBlock> bitcoinBlocks = new ArrayList<>();
            for (BitcoinBlockData data : list) {
                BitcoinBlock bitcoinBlock = BitcoinBlock.create(data.getHeight(),
                        data.getBlockHash(), data.getDifficulty(),
                        LocalDateTime.parse(data.getTimestamp().get("time").toString(), formatter));
                bitcoinBlocks.add(bitcoinBlock);
                lastHeight = Math.max(lastHeight, bitcoinBlock.getHeight());
                bitcoinBlockRepository.save(bitcoinBlock);
                makeBitcoinPrediction(lastHeight);
            }
            batchJob.updateHeight(lastHeight);
            bitcoinBlockBatchJobRepository.save(batchJob);
        }
    }

    @Transactional
    public void makeBitcoinPrediction(int nowHeight){
        BitcoinBlock lastBitcoinBlock = bitcoinBlockRepository.getLastBitcoinBlock();
        int lastBlockHeight = lastBitcoinBlock.getHeight();
        int lastChangeBlockHeight = lastBlockHeight - (lastBlockHeight % 2016);

        if (lastBlockHeight % 2016 == 0) {
            return;
        }

        BitcoinBlock lastChange = bitcoinBlockRepository.getBitcoinBlockByHeight(lastChangeBlockHeight);

        double nowTakesMineTime = Duration.between(lastChange.getMined(), lastBitcoinBlock.getMined()).toSeconds() /
                (double) (lastBitcoinBlock.getHeight() - lastChange.getHeight());

        double newPredictRatio = 600 / nowTakesMineTime;

        BigInteger newPrediction = BigDecimal.valueOf(lastBitcoinBlock.getDifficulty())
                .multiply(new BigDecimal(newPredictRatio)).toBigInteger();

        String predictionDifficulty = convertString(newPrediction.toString());
        int predictionHeight = lastChangeBlockHeight + 2016;
        double mineAverageTime = Math.round((nowTakesMineTime / 60) * 100) / 100.0;
        double difficultyChangePercent = Math.round(((newPredictRatio * 100) - 100) * 100) / 100.0;

        if (nowHeight % 2016 == 1) {
            BitcoinBlockPrediction bitcoinBlockPrediction = BitcoinBlockPrediction.create(
                    predictionHeight, predictionDifficulty, mineAverageTime, difficultyChangePercent);
            bitcoinBlockPredictionRepository.save(bitcoinBlockPrediction);

        } else if (nowHeight % 2016 > 1) {
            BitcoinBlockPrediction bitcoinBlockPrediction = bitcoinBlockPredictionRepository.getBitcoinBlockPrediction();
            bitcoinBlockPrediction.updatePrediction(predictionDifficulty, mineAverageTime, difficultyChangePercent);
            bitcoinBlockPredictionRepository.save(bitcoinBlockPrediction);

        }

    }

    private String convertString(String strSize) {
        if (strSize == null || strSize.length() == 0) {
            return "0 B";
        }

        long nSize = 0;
        String resultSize = "";

        try {
            nSize = Long.parseLong(strSize);

            DecimalFormat df = new DecimalFormat("#,###.##");

            String[] arrUnit = {"B", "K", "M", "G", "T", "P"};
            long nUnit = 1000;
            long nSection = 1;
            for (int i=0; i<6; i++) {
                if (nSize < nSection * nUnit) {
                    resultSize = df.format(nSize * 1.00 / nSection) + arrUnit[i];
                    break;
                }

                nSection *= nUnit;
            }

        } catch (Exception e) {
            resultSize = strSize + " B";

        }

        if (resultSize.length() == 0) {
            resultSize = strSize + " B";
        }

        return resultSize;
    }
}
