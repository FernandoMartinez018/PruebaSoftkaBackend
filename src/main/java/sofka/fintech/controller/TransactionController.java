package sofka.fintech.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.fintech.dto.TransactionRequest;
import sofka.fintech.dto.TransactionResponse;
import sofka.fintech.service.TransactionService;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        log.info("Recibida solicitud de transacción: {}", request);
        return transactionService.createTransaction(request);
    }

    @GetMapping
    public Flux<TransactionResponse> getAllTransactions() {
        log.info("Recibida solicitud para listar transacciones");
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Mono<TransactionResponse> getTransactionById(@PathVariable Long id) {
        log.info("Recibida solicitud para transacción ID: {}", id);
        return transactionService.getTransactionById(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionResponse> streamTransactions() {
        log.info("Cliente conectado al stream de transacciones");
        return transactionService.getAllTransactions();
    }
}