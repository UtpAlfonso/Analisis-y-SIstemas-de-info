package com.sportplay.service;

import com.sportplay.model.Producto;
import com.sportplay.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Devuelve una página de productos, opcionalmente filtrada por un término de búsqueda.
     */
    public Page<Producto> listarProductosPaginados(Pageable pageable, String busqueda) {
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            return productoRepository.findByNombreContainingIgnoreCase(busqueda, pageable);
        }
        return productoRepository.findAll(pageable);
    }
    
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}