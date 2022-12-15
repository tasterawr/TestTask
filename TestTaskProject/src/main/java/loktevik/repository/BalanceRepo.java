package loktevik.repository;

import loktevik.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepo extends JpaRepository<Balance, Long> {
}
