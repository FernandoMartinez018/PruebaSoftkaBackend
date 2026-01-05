package sofka.fintech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TransactionRequest {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01",message = "el monto debe ser mayor a cero")
    private BigDecimal monto;
}
