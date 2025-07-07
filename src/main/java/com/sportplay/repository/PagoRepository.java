package com.sportplay.repository;

import com.sportplay.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Por ahora, los métodos CRUD básicos son suficientes.
    // Podríamos añadir búsquedas por método de pago o estado si fuera necesario.
    // Ejemplo: List<Pago> findByMetodoPago(String metodoPago);
}