package loktevik.service.impl;

import loktevik.domain.Balance;
import loktevik.repository.BalanceRepo;
import loktevik.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepo balanceRepo;

    @Autowired
    public BalanceServiceImpl(BalanceRepo balanceRepo) {
        this.balanceRepo = balanceRepo;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        Optional<Balance> balance = balanceRepo.findById(id);
        if (balance.isPresent()){
            return Optional.of(balance.get().getAmount());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        Optional<Balance> balanceOptional = balanceRepo.findById(id);
        if (balanceOptional.isPresent()){
            Balance balance = balanceOptional.get();
            balance.setAmount(amount);
            balanceRepo.save(balance);
        } else {
            balanceRepo.save(new Balance(id, amount));
        }
    }
}
