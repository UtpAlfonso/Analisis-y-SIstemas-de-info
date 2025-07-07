package com.sportplay.repository;

import com.sportplay.model.Cliente;
import com.sportplay.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Busca todas las ventas asociadas a un cliente específico.
     * Spring Data JPA entiende "Pedido_Cliente" como una navegación a través de la relación Venta -> Pedido -> Cliente.
     * @param cliente El cliente cuyo historial de ventas se quiere obtener.
     * @return una lista de ventas para el cliente dado.
     */
    List<Venta> findByPedido_Cliente(Cliente cliente);

    /**
     * Busca todas las ventas realizadas dentro de un rango de fechas.
     * Esencial para generar reportes de ventas.
     * @param fechaInicio La fecha y hora de inicio del rango.
     * @param fechaFin La fecha y hora de fin del rango.
     * @return una lista de ventas dentro del rango especificado.
     */
    List<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}