package com.charlyCorporation.productos;

import com.charlyCorporation.productos.controller.ProductosController;
import com.charlyCorporation.productos.model.Producto;
import com.charlyCorporation.productos.service.ProdImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductosController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdImp productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGuardarProductoExitosamente() throws Exception {
        Producto producto = new Producto(null, "Laptop", "Lenovo", 15000.0);

        Mockito.when(productoService.saveProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/producto/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.marca").value("Lenovo"))
                .andExpect(jsonPath("$.precio").value(15000.0));
    }

    @Test
    void testGuardarProductoConCamposInvalidos() throws Exception {
        // Producto sin nombre ni marca ni precio (inválido)
        Producto producto = new Producto(null, "", "", null);

        mockMvc.perform(post("/producto/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.marca").exists())
                .andExpect(jsonPath("$.precio").exists());
    }

    @Test
    void testObtenerTodosLosProductos() throws Exception {
        Producto producto = new Producto(1L, "Tablet", "Samsung", 8000.0);

        Mockito.when(productoService.getProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/producto/listProductos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Tablet"))
                .andExpect(jsonPath("$[0].marca").value("Samsung"))
                .andExpect(jsonPath("$[0].precio").value(8000.0));
    }

    @Test
    void testBuscarProductoPorId() throws Exception {
        Producto producto = new Producto(1L, "Monitor", "LG", 4000.0);

        Mockito.when(productoService.findProducto(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/producto/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor"))
                .andExpect(jsonPath("$.marca").value("LG"));
    }

    @Test
    void testBuscarProductoPorNombre() throws Exception {
        Producto producto = new Producto(2L, "Mouse", "Logitech", 300.0);

        Mockito.when(productoService.findProductoByNombre("Mouse")).thenReturn(Optional.of(List.of(producto)));

        mockMvc.perform(get("/producto/findByNombre/Mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mouse"))
                .andExpect(jsonPath("$[0].marca").value("Logitech"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        Producto producto = new Producto(2L, "Teclado", "Microsoft", 500.0);

        Mockito.when(productoService.findProducto(2L)).thenReturn(Optional.of(producto));
        Mockito.doNothing().when(productoService).deleteProducto(2L);

        mockMvc.perform(delete("/producto/delete/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Exito en la eliminacion del producto"));
    }
}
