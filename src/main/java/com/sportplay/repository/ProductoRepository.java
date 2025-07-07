package com.sportplay.repository;

import com.sportplay.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos cuyo nombre contenga el término de búsqueda, ignorando mayúsculas y minúsculas.
     * Devuelve los resultados en un objeto Page, que incluye la información de paginación.
     * @param nombre El término de búsqueda para el nombre del producto.
     * @param pageable Objeto que contiene la información de paginación (número de página, tamaño, orden).
     * @return una página (Page) de productos que coinciden con la búsqueda.
     */
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
