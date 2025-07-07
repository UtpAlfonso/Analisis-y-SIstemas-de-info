package com.sportplay.repository;

import com.sportplay.model.Cliente;
import com.sportplay.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca un pedido de un cliente específico que tenga un estado determinado.
     * Es ideal para encontrar el "CARRITO" activo de un usuario.
     * @param cliente El cliente al que pertenece el pedido.
     * @param estado El estado del pedido a buscar (e.g., "CARRITO").
     * @return un Optional que contiene el Pedido si se encuentra.
     */
    Optional<Pedido> findByClienteAndEstado(Cliente cliente, String estado);
}