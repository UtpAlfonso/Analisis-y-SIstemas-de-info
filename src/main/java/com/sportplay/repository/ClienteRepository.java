package com.sportplay.repository;

import com.sportplay.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su dirección de correo electrónico.
     * Es fundamental para el proceso de login y para verificar si un email ya está registrado.
     * @param email El email a buscar.
     * @return un Optional que contiene el Cliente si se encuentra, o un Optional vacío si no.
     */
    Optional<Cliente> findByEmail(String email);
}