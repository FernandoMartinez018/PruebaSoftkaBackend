package sofka.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.fintech.domain.Transaction;
import sofka.fintech.dto.TransactionRequest;
import sofka.fintech.dto.TransactionResponse;
import sofka.fintech.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final CommisionService commissionService;

    public Mono<TransactionResponse> createTransaction(TransactionRequest request) {
        log.info("Creando transacción para monto: {}", request.getMonto());

        return Mono.fromSupplier(() -> {
                    BigDecimal comision = commissionService.calculateCommission(request.getMonto());

                    Transaction transaction = Transaction.builder()
                            .monto(request.getMonto())
                            .comision(comision)
                            .fecha(LocalDateTime.now())
                            .build();

                    return transaction;
                })
                .flatMap(repository::save)
                .map(this::mapToResponse)
                .doOnSuccess(response ->
                        log.info("Transacción creada exitosamente: ID {}", response.getId()))
                .doOnError(error ->
                        log.error("Error al crear transacción", error));
    }

    public Flux<TransactionResponse> getAllTransactions() {
        log.info("Obteniendo todas las transacciones");

        return repository.findAllByOrderByFechaDesc()
                .map(this::mapToResponse)
                .doOnComplete(() -> log.info("Transacciones recuperadas exitosamente"));
    }

    public Mono<TransactionResponse> getTransactionById(Long id) {
        log.info("Buscando transacción con ID: {}", id);

        return repository.findById(id)
                .map(this::mapToResponse)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Transacción no encontrada con ID: " + id)));
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        String percentage = commissionService.getCommissionPercentage(transaction.getMonto());

        return TransactionResponse.builder()
                .id(transaction.getId())
                .monto(transaction.getMonto())
                .comision(transaction.getComision())
                .fecha(transaction.getFecha())
                .mensaje(String.format("Comisión aplicada: %s", percentage))
                .build();
    }
}
