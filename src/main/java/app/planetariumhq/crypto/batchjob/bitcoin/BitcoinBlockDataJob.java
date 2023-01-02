package app.planetariumhq.crypto.batchjob.bitcoin;

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
            BitcoinBlockPredictionRepository bitcoinBlockPredictionRepository, @Qualifier("bitqueryBitcoinDataServiceImpl") BitcoinDataService bitqueryBitcoinDataService) {
        this.bitcoinBlockBatchJobRepository = blockBatchJobRepository;
        this.bitcoinBlockRepository = bitcoinBlockRepository;
        this.bitcoinBlockPredictionRepository = bitcoinBlockPredictionRepository;
        this.bitqueryBitcoinDataService = bitqueryBitcoinDataService;
    }

    // 마지막 DB저장된 블록부터 최근 블록까지 파싱 후 DB저장
    // 예측되는 블록 난이도 계산
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
            for (BitcoinBlockData data : list) {

                BitcoinBlock bitcoinBlock = BitcoinBlock.create(
                        data.getHeight(),
                        data.getBlockHash(),
                        data.getDifficulty(),
                        LocalDateTime.parse(data.getTimestamp().get("time").toString(), formatter),
                        data.getTransactionCount());

                lastHeight = Math.max(lastHeight, bitcoinBlock.getHeight());
                bitcoinBlockRepository.save(bitcoinBlock);

                makeBitcoinPrediction(lastHeight);
            }

            batchJob.updateHeight(lastHeight);
            bitcoinBlockBatchJobRepository.save(batchJob);
        }
    }

    // 채굴된 블록까지 예상치 계산
    @Transactional
    public void makeBitcoinPrediction(int nowHeight){
        //최근 채굴된 블록
        BitcoinBlock lastBlock = bitcoinBlockRepository.getLastBitcoinBlock();
        int lastBlockHeight = lastBlock.getHeight();
        //최근 난이도 변경된 블록 높이
        int lastChangeBlockHeight = lastBlockHeight - (lastBlockHeight % 2016);
        BitcoinBlock lastChangeBlock = bitcoinBlockRepository.getBitcoinBlockByHeight(lastChangeBlockHeight);

        if (lastBlockHeight % 2016 == 0) {
            return;
        }
        // 블록당 평균 채굴시간(초)
        double avgSecondsPerBlock = Duration.between(lastChangeBlock.getMined(), lastBlock.getMined()).toSeconds() /
                (double) (lastBlock.getHeight() - lastChangeBlock.getHeight());

        // 의도한 평균 채굴시간 / 실제 평균 채굴 시간
        double predictRatio = 600 / avgSecondsPerBlock;

        // 현재 블록난이도 / (의도한 평균 채굴시간 / 실제 평균 채굴 시간) = 예측되는 난이도
        BigInteger predictionDifficulty = BigDecimal.valueOf(lastBlock.getDifficulty())
                .multiply(new BigDecimal(predictRatio)).toBigInteger();
        String newPredictionDifficulty = convertString(predictionDifficulty.toString());
        String presentDifficulty = convertString(BigDecimal.valueOf(lastBlock.getDifficulty()).toBigInteger().toString());
        //다음 난이도 변경 높이
        int predictionHeight = lastChangeBlockHeight + 2016;

        // 평균 채굴 시간 (분)
        double mineAverageTime = Math.round((avgSecondsPerBlock / 60) * 100) / 100.0;

        // 예상되는 난이도 변경 시간
        LocalDateTime predictionChangeDate = lastBlock.getMined().plusMinutes(
                (long) (mineAverageTime * (predictionHeight - nowHeight)));

        // 직전난이도 대비 난이도 변경 퍼센트
        double difficultyChangePercent = Math.round(((predictRatio * 100) - 100) * 100) / 100.0;

        if (nowHeight % 2016 == 1) {
            BitcoinBlockPrediction bitcoinBlockPrediction = BitcoinBlockPrediction.create(
                    predictionHeight, newPredictionDifficulty, mineAverageTime, difficultyChangePercent, predictionChangeDate, presentDifficulty);
            bitcoinBlockPredictionRepository.save(bitcoinBlockPrediction);

        } else if (nowHeight % 2016 > 1) {
            BitcoinBlockPrediction bitcoinBlockPrediction = bitcoinBlockPredictionRepository.getBitcoinBlockPrediction();
            bitcoinBlockPrediction.updatePrediction(newPredictionDifficulty, mineAverageTime, difficultyChangePercent, predictionChangeDate, presentDifficulty);
            bitcoinBlockPredictionRepository.save(bitcoinBlockPrediction);

        }

    }

    // 읽기 편한 숫자로 convert
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
            for (int i = 0; i < 6; i++) {
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
