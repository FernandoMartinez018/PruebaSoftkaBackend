-- Tabla de transacciones
DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              monto DECIMAL(15, 2) NOT NULL,
                              comision DECIMAL(15, 2) NOT NULL,
                              fecha TIMESTAMP NOT NULL,
                              CONSTRAINT chk_monto CHECK (monto > 0),
                              CONSTRAINT chk_comision CHECK (comision >= 0)
);

-- Índice para mejorar consultas por fecha
CREATE INDEX idx_transactions_fecha ON transactions(fecha DESC);

-- Datos de prueba (opcional)
INSERT INTO transactions (monto, comision, fecha) VALUES
                                                      (5000.00, 100.00, CURRENT_TIMESTAMP),
                                                      (15000.00, 750.00, CURRENT_TIMESTAMP),
                                                      (8500.50, 170.01, CURRENT_TIMESTAMP);