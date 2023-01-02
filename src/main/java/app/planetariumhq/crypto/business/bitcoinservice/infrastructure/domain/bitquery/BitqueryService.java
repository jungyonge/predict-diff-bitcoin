package app.planetariumhq.crypto.business.bitcoinservice.infrastructure.domain.bitquery;

import app.planetariumhq.crypto.business.bitcoinservice.domain.bitcoin.BitcoinBlockData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;



@Slf4j
@Component
public class BitqueryService {

    private final RestTemplate restTemplate;
    private final String API_KEY;

    private final String API_URL;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static String BLOCK_LIST_QUERY = "{\n"
                                                + "  bitcoin(network: bitcoin) {\n"
                                                + "    blocks(height: {gt: %d}) {\n"
                                                + "      blockHash(blockHash: {})\n"
                                                + "      height\n"
                                                + "      difficulty\n"
                                                + "      timestamp {\n"
                                                + "        time\n"
                                                + "        unixtime\n"
                                                + "      }"
                                                + "    }\n"
                                                + "  }\n"
                                                + "}\n";

    private final static String BLOCK_QUERY = "{\n"
                                            + "  bitcoin(network: bitcoin) {\n"
                                            + "    blocks(height: {is: %d}) {\n"
                                            + "      blockHash(blockHash: {})\n"
                                            + "      height\n"
                                            + "      difficulty\n"
                                            + "      timestamp {\n"
                                            + "        time\n"
                                            + "        unixtime\n"
                                            + "      }"
                                            + "    }\n"
                                            + "  }\n"
                                            + "}\n";


    public BitqueryService(RestTemplate restTemplate,
            @Value("${bitquery.api-key}") String api_key,
            @Value("${bitquery.api-url}") String api_url) {
        this.restTemplate = restTemplate;
        API_KEY = api_key;
        API_URL = api_url;
    }

    public BitcoinBlockData getBitcoinBlock(int height) {
        List<BitcoinBlockData> result;

        String query = String.format(BLOCK_QUERY, height);
        String parseString = getResponse(query);
        try {
            result = objectMapper.readValue(parseString, new TypeReference<List<BitcoinBlockData>>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result.get(0);
    }

    public List<BitcoinBlockData> getBitcoinBlockList(int startHeight) {

        List<BitcoinBlockData> result;

        String query = String.format(BLOCK_LIST_QUERY, startHeight);
        String parseString = getResponse(query);
        try {
            result = objectMapper.readValue(parseString, new TypeReference<List<BitcoinBlockData>>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String getResponse(String query){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", API_KEY);
        headers.add("content-type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("query", query);
        List<BitcoinBlockData> result;
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, new HttpEntity<>(map, headers), String.class);
        String parseString = response.getBody().substring(response.getBody().indexOf("["), response.getBody().indexOf("]") + 1);
        return parseString;
    }
}
