package com.sportplay.controller;

import com.sportplay.model.Producto;
import com.sportplay.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String busqueda,
            Model model) {
        Page<Producto> paginaProductos = productoService.listarProductosPaginados(PageRequest.of(page, size), busqueda);
        model.addAttribute("paginaProductos", paginaProductos);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("currentPage", page);
        return "public/catalogo";
    }

    @GetMapping("/producto/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        model.addAttribute("producto", producto);
        return "public/detalle-producto";
    }
}