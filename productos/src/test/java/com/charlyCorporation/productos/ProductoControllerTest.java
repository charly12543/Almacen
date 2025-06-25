package com.charlyCorporation.productos;

import com.charlyCorporation.productos.model.Producto;
import com.charlyCorporation.productos.service.ProdImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  Pruebas de integración (HTTP → Controller → Service → Repository → MySQL local).
 *  ▸ Cada método se ejecuta dentro de una transacción y se revierte (@Rollback),
 *    de modo que el estado de la BD queda igual al terminar.
 *  ▸ No se usa Mockito ni Testcontainers.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")   // --> lee application-test.properties
@Transactional @Rollback
class ProductoControllerTest {

    /* ---------- Inyecciones ---------- */
    @Autowired MockMvc mockMvc;
    @Autowired ProdImp productoService;
    @Autowired ObjectMapper mapper;

    /* ---------- CREATE ---------- */

    @Test
    @DisplayName("POST /producto/save ➜ 201 Created (OK)")
    void guardarProductoExitosamente() throws Exception {
        Producto dto = new Producto(null, "Laptop", "Lenovo", 15_000.0);

        mockMvc.perform(post("/producto/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto", notNullValue()))
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.marca").value("Lenovo"))
                .andExpect(jsonPath("$.precio").value(15_000.0));
    }

    @Test
    @DisplayName("POST /producto/save ➜ 400 Bad Request (campos inválidos)")
    void guardarProductoConCamposInvalidos() throws Exception {
        Producto invalido = new Producto(null, "", "", null);

        mockMvc.perform(post("/producto/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.marca").exists())
                .andExpect(jsonPath("$.precio").exists());
    }

    /* ---------- READ ---------- */

    @Test
    @DisplayName("GET /producto/listProductos ➜ 200 OK lista completa")
    void obtenerTodosLosProductos() throws Exception {
        productoService.saveProducto(new Producto(null, "Tablet", "Samsung", 8_000.0));

        mockMvc.perform(get("/producto/listProductos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Tablet"))
                .andExpect(jsonPath("$[0].marca").value("Samsung"))
                .andExpect(jsonPath("$[0].precio").value(8_000.0));
    }

    @Test
    @DisplayName("GET /producto/find/{id} ➜ 200 OK si existe")
    void buscarProductoPorId() throws Exception {
        Producto guardado = productoService.saveProducto(
                new Producto(null, "Monitor", "LG", 4_000.0));

        mockMvc.perform(get("/producto/find/{id}", guardado.getIdProducto()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(guardado.getIdProducto()))
                .andExpect(jsonPath("$.nombre").value("Monitor"))
                .andExpect(jsonPath("$.marca").value("LG"))
                .andExpect(jsonPath("$.precio").value(4_000.0));
    }

    @Test
    @DisplayName("GET /producto/findByNombre/{nombre} ➜ 200 OK lista filtrada")
    void buscarProductoPorNombre() throws Exception {
        productoService.saveProducto(new Producto(null, "Mouse", "Logitech", 300.0));

        mockMvc.perform(get("/producto/findByNombre/{nombre}", "Mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse"))
                .andExpect(jsonPath("$[0].marca").value("Logitech"))
                .andExpect(jsonPath("$[0].precio").value(300.0));
    }

    /* ---------- DELETE ---------- */

    @Test
    @DisplayName("DELETE /producto/delete/{id} ➜ 200 OK al eliminar")
    void eliminarProducto() throws Exception {
        Producto guardado = productoService.saveProducto(
                new Producto(null, "Teclado", "Microsoft", 500.0));

        mockMvc.perform(delete("/producto/delete/{id}", guardado.getIdProducto()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsStringIgnoringCase("exito")));
    }
}
