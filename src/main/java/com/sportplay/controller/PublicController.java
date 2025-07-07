package com.sportplay.controller;

import com.sportplay.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    @GetMapping("/")
    public String mostrarHomePage() {
        // Podrías añadir lógica para mostrar productos destacados en la página principal
        return "public/index"; // Devuelve la vista de la página de inicio
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "registroExitoso", required = false) String registroExitoso,
                               Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Email o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Has cerrado sesión exitosamente.");
        }
        if (registroExitoso != null) {
            model.addAttribute("registroExitoso", "¡Registro exitoso! Por favor, inicia sesión.");
        }
        return "auth/login"; // Devuelve la vista del formulario de login
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        // Se pasa un objeto Cliente vacío para enlazarlo con el formulario (th:object)
        model.addAttribute("cliente", new Cliente());
        return "auth/registro"; // Devuelve la vista del formulario de registro
    }
}