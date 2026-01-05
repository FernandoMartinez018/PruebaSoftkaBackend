package sofka.fintech.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sofka.fintech.domain.Transaction;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {

    Flux<Transaction> findAllByOrderByFechaDesc();
}
