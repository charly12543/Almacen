package com.charlyCorporation.carrito_compras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase DTO que contiene los atributos del carrito de compras
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {

    private Long idCarrito;
    private Double precioTotal;
    String nomProductos;
    private List<ProductosDTO> listProductos;
}
