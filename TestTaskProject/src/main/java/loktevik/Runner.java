package loktevik;

import loktevik.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final BalanceService balanceService;

    @Override
    public void run(String... args) throws Exception {
        balanceService.changeBalance(123456789L, 10000L);
        balanceService.changeBalance(113456789L, 10000L);
        balanceService.changeBalance(123451719L, 10000L);
        balanceService.changeBalance(123446789L, 10000L);
        balanceService.changeBalance(123466789L, 10000L);
        balanceService.changeBalance(122256789L, 10000L);
        balanceService.changeBalance(123356489L, 10000L);
        balanceService.changeBalance(123656589L, 10000L);
    }
}