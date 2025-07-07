package com.sportplay.controller;

import com.sportplay.model.Cliente;
import com.sportplay.model.Pedido;
import com.sportplay.model.Venta;
import com.sportplay.service.ClienteService;
import com.sportplay.service.PedidoService;
import com.sportplay.service.VentaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VentaController {

    private final VentaService ventaService;
    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public VentaController(VentaService ventaService, PedidoService pedidoService, ClienteService clienteService) {
        this.ventaService = ventaService;
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }
    
    private Cliente getClienteAutenticado(Authentication authentication) {
        return clienteService.buscarPorEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado."));
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(Authentication authentication, Model model) {
        Cliente cliente = getClienteAutenticado(authentication);
        Pedido carrito = pedidoService.getCarritoActivo(cliente);
        if(carrito.getDetalles().isEmpty()) {
            return "redirect:/carrito"; // No se puede hacer checkout con carrito vacío
        }
        model.addAttribute("carrito", carrito);
        return "public/checkout";
    }

    @PostMapping("/checkout/procesar")
    public String procesarVenta(Authentication authentication, @RequestParam String metodoPago, RedirectAttributes redirectAttributes) {
        Cliente cliente = getClienteAutenticado(authentication);
        Pedido carrito = pedidoService.getCarritoActivo(cliente);

        try {
            Venta nuevaVenta = ventaService.procesarVenta(carrito.getId(), metodoPago);
            // Aquí se podría enviar un email de confirmación.
            redirectAttributes.addFlashAttribute("venta", nuevaVenta);
            return "redirect:/venta/confirmacion";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito";
        }
    }
    
    @GetMapping("/venta/confirmacion")
    public String mostrarConfirmacion() {
        // La vista leerá el objeto "venta" de los flash attributes
        return "public/confirmacion-compra";
    }
    
    // El historial de ventas podría ir en el ClienteController o aquí
    // @GetMapping("/perfil/historial") ...
}
