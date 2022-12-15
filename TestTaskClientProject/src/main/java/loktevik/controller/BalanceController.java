package loktevik.controller;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/")
public class BalanceController {

    private final Logger log = Logger.getLogger(BalanceController.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<Long> readIdList = Arrays.asList(123456789L,
            123451719L,
            123446789L,
            123466789L,
            123356489L,
            123656589L);

    private final List<Long> writeIdList = Arrays.asList(113456789L,
            123451719L,
            123466789L,
            122256789L,
            123356489L,
            123656589L);

    private final List<Long> amountList = Arrays.asList(100000L,
            55000L,
            230000L,
            12000L,
            450000L,
            111000L);

    @GetMapping("test-balance-service")
    public void testBalanceService(@RequestParam Integer threadCount,
                                   @RequestParam Integer readQuota,
                                   @RequestParam Integer writeQuota){


        for (int i = 0; i < threadCount; i++){
            Thread thread = new Thread (() -> {
                while (true) {
                    double readProbability = (double)readQuota/(double)(readQuota+writeQuota);

                    if (ThreadLocalRandom.current().nextDouble() < readProbability) {
                        getBalance(randomFromList(readIdList));
                    } else {
                        changeBalance(randomFromList(writeIdList), randomFromList(amountList));
                    }
                }
            });
            thread.start();
        }
    }

    @SneakyThrows
    public void getBalance(Long id){
        String uriString = "http://localhost:8081/balance/" + id;
        String getResult = restTemplate.getForObject(uriString, String.class);
        log.info(String.format("GET id: %d, amount: %s", id, getResult));
    }

    public void changeBalance(Long id, Long amount){
        String uriString = String.format("http://localhost:8081/balance/%d?amount=%d", id, amount);
        ResponseEntity<String> exchange = restTemplate.exchange(uriString, HttpMethod.PUT, null, String.class);
        log.info(String.format("PUT id: %d, amount: %s", id, exchange.getBody()));
    }

    public Long randomFromList(List<Long> idList){
        int index = ThreadLocalRandom.current().nextInt(idList.size());
        return idList.get(index);
    }
}
