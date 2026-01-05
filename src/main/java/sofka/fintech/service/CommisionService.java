package sofka.fintech.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CommisionService {

    private static final BigDecimal THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal HIGH_COMMISSION_RATE = new BigDecimal("0.05");
    private static final BigDecimal LOW_COMMISSION_RATE = new BigDecimal("0.02");


    public BigDecimal calculateCommission(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }

        BigDecimal rate = isHighAmount(monto) ? HIGH_COMMISSION_RATE : LOW_COMMISSION_RATE;
        return monto.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }


    private boolean isHighAmount(BigDecimal monto) {
        return monto.compareTo(THRESHOLD) > 0;
    }


    public String getCommissionPercentage(BigDecimal monto) {
        return isHighAmount(monto) ? "5%" : "2%";
    }
}