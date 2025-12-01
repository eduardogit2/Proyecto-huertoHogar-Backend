package com.example.HuertoHogar.dto;

import com.example.HuertoHogar.model.*;
import com.example.HuertoHogar.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ResenaRepository resenaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        cargarUsuarios();
        cargarCategorias();
        cargarProductosYResenas();
    }

    private void cargarUsuarios() {
        if (userRepository.findByEmail("admin@huertohogar.cl").isEmpty()) {
            User admin = User.builder()
                    .nombre("Admin")
                    .apellidos("Sistema")
                    .rut("1-9")
                    .email("admin@huertohogar.cl")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);            
        }
    }

    private void cargarCategorias() {
        if (categoriaRepository.count() == 0) {
            List<String> nombres = Arrays.asList("Frutas", "Verduras", "Orgánicos", "Lácteos");
            for (String nombre : nombres) {
                Categoria cat = new Categoria();
                cat.setNombre(nombre);
                cat.setDescripcion("Categoría de " + nombre);
                categoriaRepository.save(cat);
            }
        }
    }

    private void cargarProductosYResenas() {
        if (productoRepository.count() == 0) {
            
            crearProducto(
                Producto.builder().nombre("Manzana Fuji").precio(1200).categoria("Frutas").imagen("img/prod1.jpg")
                .descripcion("Manzanas Fuji crujientes y dulces, cultivadas en el fértil Valle del Maule. Son perfectas para consumir como un snack saludable.").stock(150).origen("Valle del Maule, Chile").unidad("kg").build(),
                Arrays.asList(
                    Resena.builder().usuario("Ana M.").calificacion(5).texto("Excelente calidad.").build(),
                    Resena.builder().usuario("Pedro V.").calificacion(4).texto("Muy buenas.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Naranjas Valencia").precio(1000).categoria("Frutas").imagen("img/prod2.jpg")
                .descripcion("Naranjas Valencia excepcionalmente jugosas y ricas en vitamina C. Provenientes de la soleada Región de Coquimbo.").stock(200).origen("Región de Coquimbo, Chile").unidad("kg").build(),
                Arrays.asList(
                    Resena.builder().usuario("María P.").calificacion(5).texto("Muy jugosas.").build(),
                    Resena.builder().usuario("Juan F.").calificacion(4).texto("Sabor muy bueno.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Plátano Cavendish").precio(800).categoria("Frutas").imagen("img/prod3.jpg")
                .descripcion("Plátanos Cavendish maduros y naturally dulces, cultivados en la región de Guayas, Ecuador.").stock(250).origen("Guayas, Ecuador").unidad("kg").build(),
                Arrays.asList(
                    Resena.builder().usuario("Sofía G.").calificacion(5).texto("Muy frescos.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Zanahoria Orgánica").precio(900).categoria("Verduras").imagen("img/prod4.jpg")
                .descripcion("Zanahorias orgánicas y crujientes, cultivadas sin pesticidas en la Región de O'Higgins.").stock(100).origen("Región de O'Higgins, Chile").unidad("kg").build(),
                Arrays.asList(
                    Resena.builder().usuario("Carolina V.").calificacion(5).texto("Frescas y con un sabor intenso.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Espinaca Fresca").precio(700).categoria("Verduras").imagen("img/prod5.jpg")
                .descripcion("Espinacas frescas y nutritivas, cultivadas con prácticas orgánicas en la región de Ñuble, Chile.").stock(80).origen("Ñuble, Chile").unidad("bolsa").build(),
                Arrays.asList(
                    Resena.builder().usuario("Roberta A.").calificacion(4).texto("Buena cantidad.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Pimiento Tricolores").precio(1500).categoria("Orgánicos").imagen("img/prod6.jpg")
                .descripcion("Pimientos rojos, amarillos y verdes, seleccionados por su calidad y frescura.").stock(120).origen("Región de Valparaíso, Chile").unidad("kg").build(),
                Arrays.asList(
                    Resena.builder().usuario("Diego B.").calificacion(5).texto("Colores vibrantes y muy frescos.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Miel Orgánica").precio(5000).precioConDescuento(4500).categoria("Orgánicos").imagen("img/prod7.jpg").etiqueta("Oferta")
                .descripcion("Miel pura y 100% orgánica, producida por apicultores locales de la Región de Aysén.").stock(50).origen("Aysén, Chile").unidad("frasco").build(),
                Arrays.asList(
                    Resena.builder().usuario("Antonia D.").calificacion(5).texto("Un sabor exquisito.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Quínoa Orgánica").precio(4500).precioConDescuento(4000).categoria("Orgánicos").imagen("img/prod8.jpg").etiqueta("Oferta")
                .descripcion("Quínoa orgánica de alta calidad, proveniente de Cajamarca, Perú. Un superalimento versátil.").stock(75).origen("Cajamarca, Perú").unidad("bolsa").build(),
                Arrays.asList(
                    Resena.builder().usuario("Fernanda L.").calificacion(5).texto("Excelente para mis ensaladas.").build()
                )
            );

            crearProducto(
                Producto.builder().nombre("Leche Entera").precio(1400).precioConDescuento(1250).categoria("Lácteos").imagen("img/prod9.jpg").etiqueta("Oferta")
                .descripcion("Leche entera fresca y cremosa, producida en la Región de Los Lagos, Chile.").stock(90).origen("Los Lagos, Chile").unidad("litro").build(),
                Arrays.asList(
                    Resena.builder().usuario("Pablo Q.").calificacion(5).texto("Muy buena y fresca.").build()
                )
            );

            System.out.println("PRODUCTOS Y RESEÑAS COMPLETOS CARGADOS");
        }
    }

    private void crearProducto(Producto prod, List<Resena> resenas) {
        Producto guardado = productoRepository.save(prod);
        for (Resena r : resenas) {
            r.setProducto(guardado);
            resenaRepository.save(r);
        }
    }
}