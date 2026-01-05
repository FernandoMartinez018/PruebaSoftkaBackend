package sofka.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sofka.fintech.domain.Transaction;
import sofka.fintech.dto.TransactionRequest;
import sofka.fintech.dto.TransactionResponse;
import sofka.fintech.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias con Mockito para TransactionService
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private CommisionService commissionService;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionRequest request;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        request = TransactionRequest.builder()
                .monto(new BigDecimal("5000.00"))
                .build();

        transaction = Transaction.builder()
                .id(1L)
                .monto(new BigDecimal("5000.00"))
                .comision(new BigDecimal("100.00"))
                .fecha(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe crear transacción exitosamente")
    void shouldCreateTransactionSuccessfully() {
        // Given
        when(commissionService.calculateCommission(any(BigDecimal.class)))
                .thenReturn(new BigDecimal("100.00"));
        when(commissionService.getCommissionPercentage(any(BigDecimal.class)))
                .thenReturn("2%");
        when(repository.save(any(Transaction.class)))
                .thenReturn(Mono.just(transaction));

        // When
        Mono<TransactionResponse> result = transactionService.createTransaction(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getId().equals(1L) &&
                                response.getMonto().equals(new BigDecimal("5000.00")) &&
                                response.getComision().equals(new BigDecimal("100.00"))
                )
                .verifyComplete();

        verify(commissionService, times(1)).calculateCommission(any(BigDecimal.class));
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Debe obtener todas las transacciones")
    void shouldGetAllTransactions() {
        // Given
        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .monto(new BigDecimal("15000.00"))
                .comision(new BigDecimal("750.00"))
                .fecha(LocalDateTime.now())
                .build();

        when(repository.findAllByOrderByFechaDesc())
                .thenReturn(Flux.just(transaction, transaction2));
        when(commissionService.getCommissionPercentage(any(BigDecimal.class)))
                .thenReturn("2%", "5%");

        // When
        Flux<TransactionResponse> result = transactionService.getAllTransactions();

        // Then
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(repository, times(1)).findAllByOrderByFechaDesc();
    }

    @Test
    @DisplayName("Debe obtener transacción por ID")
    void shouldGetTransactionById() {
        // Given
        when(repository.findById(1L))
                .thenReturn(Mono.just(transaction));
        when(commissionService.getCommissionPercentage(any(BigDecimal.class)))
                .thenReturn("2%");

        // When
        Mono<TransactionResponse> result = transactionService.getTransactionById(1L);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getId().equals(1L))
                .verifyComplete();

        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar error cuando transacción no existe")
    void shouldThrowErrorWhenTransactionNotFound() {
        // Given
        when(repository.findById(999L))
                .thenReturn(Mono.empty());

        // When
        Mono<TransactionResponse> result = transactionService.getTransactionById(999L);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Transacción no encontrada")
                )
                .verify();
    }
}
