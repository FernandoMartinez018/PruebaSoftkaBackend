package sofka.fintech.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("TRANSACTIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private Long id;

    private BigDecimal monto;

    private BigDecimal comision;

    private LocalDateTime fecha;

    public Transaction(BigDecimal monto, BigDecimal comision, LocalDateTime fecha) {
        this.monto = monto;
        this.comision = comision;
        this.fecha = fecha;
    }
}
