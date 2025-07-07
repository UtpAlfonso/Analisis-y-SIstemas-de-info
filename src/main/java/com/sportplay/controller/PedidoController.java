package com.sportplay.controller;

import com.sportplay.model.Cliente;
import com.sportplay.model.Pedido;
import com.sportplay.service.ClienteService;
import com.sportplay.service.PedidoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    private Cliente getClienteAutenticado(Authentication authentication) {
        return clienteService.buscarPorEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado correctamente."));
    }

    @GetMapping("/carrito")
    public String mostrarCarrito(Authentication authentication, Model model) {
        Cliente cliente = getClienteAutenticado(authentication);
        Pedido carrito = pedidoService.getCarritoActivo(cliente);
        model.addAttribute("carrito", carrito);
        return "public/carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarProductoCarrito(Authentication authentication, @RequestParam Long productoId, @RequestParam(defaultValue = "1") int cantidad, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = getClienteAutenticado(authentication);
            pedidoService.agregarProductoAlCarrito(cliente, productoId, cantidad);
            redirectAttributes.addFlashAttribute("exito", "Producto agregado al carrito exitosamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al agregar el producto.");
        }
        return "redirect:/producto/" + productoId;
    }
    
    // Aquí se podrían añadir métodos para actualizar cantidad o eliminar producto del carrito
}