package com.charlyCorporation.carrito_compras.service;

import com.charlyCorporation.carrito_compras.dto.CarritoDTO;
import com.charlyCorporation.carrito_compras.dto.ProductosDTO;
import com.charlyCorporation.carrito_compras.model.Carrito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que contiene los metodos genericos
 */
public interface ICarritoService {

    public void save(Carrito car);

    public void saveCarrito(Long idCarrito,
                            String nomProductos);

    public List<Carrito> getCarrito();

    public Carrito findById(Long idCarrito);

    public Carrito addProdToCarrito(long idCarrito, String nomProductos);

    public List<ProductosDTO> findNombre(String nombre);

    public Carrito deleteProd(long idCarrito, String nomProductos);

    public void deleteAllCar(Long idCarrito);


}
