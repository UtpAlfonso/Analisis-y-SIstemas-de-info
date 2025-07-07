package com.sportplay;

import com.sportplay.model.Categoria;
import com.sportplay.model.Cliente;
import com.sportplay.model.Proveedor;
import com.sportplay.repository.CategoriaRepository;
import com.sportplay.repository.ClienteRepository;
import com.sportplay.repository.ProveedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ClienteRepository clienteRepository,
                           CategoriaRepository categoriaRepository,
                           ProveedorRepository proveedorRepository,
                           PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // Es buena práctica anotar el método con @Transactional
    public void run(String... args) throws Exception {

        // 1. Crear usuario administrador si no existe
        if (clienteRepository.findByEmail("admin@sportplay.com").isEmpty()) {
            Cliente admin = new Cliente();
            admin.setNombre("Admin");
            admin.setApellidos("SportPlay");
            admin.setEmail("admin@sportplay.com");
            admin.setPassword(passwordEncoder.encode("adminpass")); // Contraseña: adminpass
            admin.setRol("ROLE_ADMIN");
            clienteRepository.save(admin);
            System.out.println(">>> Usuario administrador por defecto creado.");
        }

        // 2. Crear usuario empleado si no existe
        if (clienteRepository.findByEmail("empleado@sportplay.com").isEmpty()) {
            Cliente empleado = new Cliente();
            empleado.setNombre("Empleado");
            empleado.setApellidos("Demo");
            empleado.setEmail("empleado@sportplay.com");
            empleado.setPassword(passwordEncoder.encode("empleadopass")); // Contraseña: empleadopass
            empleado.setRol("ROLE_EMPLEADO");
            clienteRepository.save(empleado);
            System.out.println(">>> Usuario empleado por defecto creado.");
        }

        // 3. Crear categorías si no existen
        if (categoriaRepository.count() == 0) {
            Categoria cat1 = new Categoria(null, "Calzado", "Zapatillas y calzado deportivo");
            Categoria cat2 = new Categoria(null, "Ropa", "Polos, shorts y equipamiento textil");
            Categoria cat3 = new Categoria(null, "Accesorios", "Balones, pesas y otros accesorios");
            
            categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3));
            System.out.println(">>> Categorías por defecto creadas.");
        }

        // 4. Crear proveedores si no existen
        if (proveedorRepository.count() == 0) {
            Proveedor prov1 = new Proveedor(null, "Nike", "20100123456", "Av. Principal 123", "999888777", "contacto@nike.com");
            Proveedor prov2 = new Proveedor(null, "Adidas", "20100654321", "Calle Secundaria 456", "999111222", "contacto@adidas.com");
            Proveedor prov3 = new Proveedor(null, "Puma", "20100987654", "Jirón Unión 789", "999333444", "contacto@puma.com");

            proveedorRepository.saveAll(Arrays.asList(prov1, prov2, prov3));
            System.out.println(">>> Proveedores por defecto creados.");
        }

        System.out.println(">>> Inicialización de datos completada.");
    }
}