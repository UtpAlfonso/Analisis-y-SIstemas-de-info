package com.sportplay.controller; // <-- DEBE ESTAR EN UN SUB-PAQUETE DE com.sportplay

import com.sportplay.model.Producto;
import com.sportplay.repository.*; // Importa todo el paquete de repositorios
import com.sportplay.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller; // <-- ANOTACIÓN CLAVE
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // <-- ANOTACIÓN CLAVE #1: Le dice a Spring que esta clase es un controlador.
@RequestMapping("/dashboard") // <-- ANOTACIÓN CLAVE #2: Todas las rutas aquí dentro empiezan con /dashboard
public class DashboardController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    @Autowired
    public DashboardController(ProductoService productoService, CategoriaRepository categoriaRepository,
                               ProveedorRepository proveedorRepository, ClienteRepository clienteRepository,
                               VentaRepository ventaRepository) {
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    // MÉTODO QUE MANEJA LA RUTA "/dashboard"
    @GetMapping // <-- ANOTACIÓN CLAVE #3: Se encarga de la petición GET a la raíz de /dashboard
    public String mostrarDashboard(Model model) {
        // Devuelve la plantilla que está en: templates/dashboard/index.html
        return "dashboard/index"; 
    }

    // --- Gestión de Productos ---
    @GetMapping("/productos")
    public String gestionarProductos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<Producto> paginaProductos = productoService.listarProductosPaginados(PageRequest.of(page, size), null);
        model.addAttribute("paginaProductos", paginaProductos);
        return "dashboard/productos";
    }
    
    // Y así con todos los demás métodos...
    @GetMapping("/clientes")
    public String gestionarClientes(Model model) {
        model.addAttribute("listaClientes", clienteRepository.findAll());
        return "dashboard/clientes";
    }

    @GetMapping("/ventas")
    public String gestionarVentas(Model model) {
        model.addAttribute("listaVentas", ventaRepository.findAll());
        return "dashboard/ventas";
    }
    
    // Resto de los métodos para editar, guardar, etc.
     @GetMapping({"/productos/nuevo", "/productos/editar/{id}"})
    public String mostrarFormularioProducto(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            Producto producto = productoService.obtenerProductoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            model.addAttribute("producto", producto);
        } else {
            model.addAttribute("producto", new Producto());
        }
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "dashboard/formulario-producto";
    }
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        productoService.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("exito", "Producto guardado exitosamente.");
        return "redirect:/dashboard/productos";
    }
}