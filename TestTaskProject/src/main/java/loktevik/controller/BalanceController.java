package loktevik.controller;

import loktevik.service.BalanceService;
import loktevik.service.util.BalanceServiceRequestCountTracker;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class BalanceController {
    private final BalanceServiceRequestCountTracker requestCountTracker;
    private final Logger log = Logger.getLogger(BalanceController.class);
    private final BalanceService balanceService;

    /**
     * Эндпоинт для получения информации о балансе.
     * @param id идентификатор банковского счета.
     * @return текущая сумма на банковском счету.
     */
    @GetMapping("balance/{id}")
    public String getBalance(@PathVariable Long id){
        // обновление счетчика запросов
        requestCountTracker.updateGetBalanceCnt();
        log.info(requestCountTracker.getBalanceRequestInfo());

        // непосредственное получение баланса выделено в отдельный метод,
        // чтобы задействовать кэширование. В противном случае текущий метод скипнется
        // и не обновится счетчик
        return getBalanceForId(id);
    }

    /**
     * Эндпоинт для обновления информации о балансе.
     * @param id идентификатор банковского счета.
     * @param amount новая сумма на банковском счету.
     * @return новая сумма на банковском счету.
     */
    @PutMapping("balance/{id}")
    @CachePut(value = "balances", key = "#id")
    public String changeBalance(@PathVariable Long id, @RequestParam Long amount){
        // обновление счетчика запросов
        requestCountTracker.updateChangeBalanceCnt();
        log.info(requestCountTracker.changeBalanceRequestInfo());

        balanceService.changeBalance(id, amount);
        return String.valueOf(amount);
    }

    /**
     * Возвращение текущего баланса из базы данных или из кэша.
     * @param id идентификатор банковского счета.
     * @return текущая сумма на банковском счету.
     */
    @Cacheable(value = "balances", key = "#id")
    public String getBalanceForId(Long id){
        Optional<Long> balance = balanceService.getBalance(id);
        if (balance.isPresent()){
            return String.valueOf(balance.get());
        } else
            return "Invalid id was passed.";
    }
}
