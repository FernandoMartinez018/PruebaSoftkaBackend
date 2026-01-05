package sofka.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CommissionService
 */
class CommissionServiceTest {

    private CommisionService commissionService;

    @BeforeEach
    void setUp() {
        commissionService = new CommisionService();
    }

    @Test
    @DisplayName("Debe calcular 2% para montos menores o iguales a 10,000")
    void shouldCalculateTwoPercentForLowAmounts() {
        // Given
        BigDecimal monto = new BigDecimal("5000.00");
        BigDecimal expectedComision = new BigDecimal("100.00");

        // When
        BigDecimal actualComision = commissionService.calculateCommission(monto);

        // Then
        assertEquals(expectedComision, actualComision);
    }

    @Test
    @DisplayName("Debe calcular 5% para montos mayores a 10,000")
    void shouldCalculateFivePercentForHighAmounts() {
        // Given
        BigDecimal monto = new BigDecimal("20000.00");
        BigDecimal expectedComision = new BigDecimal("1000.00");

        // When
        BigDecimal actualComision = commissionService.calculateCommission(monto);

        // Then
        assertEquals(expectedComision, actualComision);
    }

    @Test
    @DisplayName("Debe calcular 2% exactamente en el umbral de 10,000")
    void shouldCalculateTwoPercentAtThreshold() {
        // Given
        BigDecimal monto = new BigDecimal("10000.00");
        BigDecimal expectedComision = new BigDecimal("200.00");

        // When
        BigDecimal actualComision = commissionService.calculateCommission(monto);

        // Then
        assertEquals(expectedComision, actualComision);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el monto es nulo")
    void shouldThrowExceptionWhenMontoIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            commissionService.calculateCommission(null);
        });
    }

    @Test
    @DisplayName("Debe redondear correctamente a 2 decimales")
    void shouldRoundCorrectlyToTwoDecimals() {
        // Given
        BigDecimal monto = new BigDecimal("5555.55");
        BigDecimal expectedComision = new BigDecimal("111.11");

        // When
        BigDecimal actualComision = commissionService.calculateCommission(monto);

        // Then
        assertEquals(expectedComision, actualComision);
    }

    @Test
    @DisplayName("Debe retornar '2%' para montos bajos")
    void shouldReturnTwoPercentStringForLowAmounts() {
        // Given
        BigDecimal monto = new BigDecimal("5000.00");

        // When
        String percentage = commissionService.getCommissionPercentage(monto);

        // Then
        assertEquals("2%", percentage);
    }

    @Test
    @DisplayName("Debe retornar '5%' para montos altos")
    void shouldReturnFivePercentStringForHighAmounts() {
        // Given
        BigDecimal monto = new BigDecimal("15000.00");

        // When
        String percentage = commissionService.getCommissionPercentage(monto);

        // Then
        assertEquals("5%", percentage);
    }
}