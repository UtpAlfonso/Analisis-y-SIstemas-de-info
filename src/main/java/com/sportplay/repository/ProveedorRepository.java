package com.sportplay.repository;

import com.sportplay.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Métodos CRUD básicos disponibles por defecto.
}