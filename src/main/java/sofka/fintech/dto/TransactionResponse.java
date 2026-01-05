package sofka.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TransactionResponse {

    private Long id;
    private BigDecimal monto;
    private BigDecimal comision;
    private LocalDateTime fecha;
    private String mensaje;
}
