package app.planetariumhq.crypto.batchjob.bitcoin;

import app.planetariumhq.crypto.business.bitcoinservice.application.bitcoin.GetBitcoinBlockHandler;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlock;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJob;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockBatchJobRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockData;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockRepository;
import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinDataService;
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

    private final GetBitcoinBlockHandler getBitcoinBlockHandler;

    private final BitcoinDataService bitqueryBitcoinDataService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public BitcoinBlockDataJob(BitcoinBlockBatchJobRepository blockBatchJobRepository,
            BitcoinBlockRepository bitcoinBlockRepository,
            GetBitcoinBlockHandler getBitcoinBlockHandler,
            @Qualifier("bitqueryBitcoinDataServiceImpl") BitcoinDataService bitqueryBitcoinDataService) {
        this.bitcoinBlockBatchJobRepository = blockBatchJobRepository;
        this.bitcoinBlockRepository = bitcoinBlockRepository;
        this.getBitcoinBlockHandler = getBitcoinBlockHandler;
        this.bitqueryBitcoinDataService = bitqueryBitcoinDataService;
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void bitcoinBlockDataBatchJob() {

        int lastHeight = 0;
        BitcoinBlockBatchJob batchJob = bitcoinBlockBatchJobRepository.getBitcoinBlockBatchJob();
        List<BitcoinBlockData> list = bitqueryBitcoinDataService.getBitcoinBlockList(
                batchJob.getLastBitcoinHeight());
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
            }
            batchJob.updateHeight(lastHeight);
            bitcoinBlockBatchJobRepository.save(batchJob);
            bitcoinBlockRepository.saveAll(bitcoinBlocks);

        }

    }
}
