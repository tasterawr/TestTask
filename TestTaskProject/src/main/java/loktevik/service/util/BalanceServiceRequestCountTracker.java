package loktevik.service.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс для подсчета общего количества запросов и за промежуток времени (в секунду).
 **/
@Component
public class BalanceServiceRequestCountTracker {
    private int getBalanceCntTotal = 0;
    private int getBalanceCntPrev = 0;
    private int changeBalanceCntTotal = 0;
    private int changeBalanceCntPrev = 0;
    private int getBalanceCntCur = 0;
    private int changeBalanceCntCur = 0;

    /**
     * Возвращение информации о числе запросов получения информации о балансе банковского счета.
     * @return строка, с информацией о числе запросов.
     */
    public String getBalanceRequestInfo(){
        return String.format("Total number of get balance requests: %d " +
                "with %d requests per second.", getBalanceCntTotal, getBalanceCntCur);
    }

    /**
     * Возвращение информации о числе запросов изменения информации о балансе банковского счета.
     * @return строка, с информацией о числе запросов.
     */
    public String changeBalanceRequestInfo(){
        return String.format("Total number of change balance requests: %d " +
                "with %d requests per second.", changeBalanceCntTotal, changeBalanceCntCur);
    }

    /**
     * Обновление счетчика запросов на получение информации о балансе.
     */
    public synchronized void updateGetBalanceCnt(){
        getBalanceCntTotal++;
    }

    /**
     * Обновление счетчика запросов на обновление информации о балансе.
     */
    public synchronized void updateChangeBalanceCnt(){
        changeBalanceCntTotal++;
    }

    /**
     * Метод, который каждую секунду обновляет счетчики запросов, пришедших за последнюю секунду.
     */
    @PostConstruct
    public void execute(){
        Runnable runnable = () -> {
            getBalanceCntCur = getBalanceCntTotal - getBalanceCntPrev;
            changeBalanceCntCur = changeBalanceCntTotal - changeBalanceCntPrev;

            getBalanceCntPrev = getBalanceCntTotal;
            changeBalanceCntPrev = changeBalanceCntTotal;
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }
}
