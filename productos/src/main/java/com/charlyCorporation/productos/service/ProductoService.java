package com.charlyCorporation.productos.service;

import com.charlyCorporation.productos.model.Producto;
import com.charlyCorporation.productos.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase que implementa los metodos de la interfaz IProductoService
 */
@Service
public class ProductoService implements IProductoService{

    /**
     * Inyeccion de dependecia
     */
    @Autowired
    private IProductoRepository repo;


    /**
     * Metodo que muestra el listado de todos los productos existentes
     * @return
     */
    @Override
    public List<Producto> getProductos() {
        List<Producto> listaProd = repo.findAll();
        return listaProd;
    }

    /**
     * Metodo que guarda un producto
     * @param producto
     */
    @Override
    public void saveProducto(Producto producto) {
        repo.save(producto);

    }

    /**
     * Metodo para encontrar un producto especifico por su Id
     * @param idProducto
     * @return
     */
    @Override
    public Producto findProducto(Long idProducto) {

        return repo.findById(idProducto).orElseGet(null);
    }

    /**
     * Metodo para eliminar un producto por su Id
     * @param idProducto
     */
    @Override
    public void deleteProducto(Long idProducto) {
        repo.deleteById(idProducto);

    }

    /**
     * Metodo que permite editar la informacion de un producto
     * @param idProducto
     * @param nombre
     * @param marca
     * @param precio
     * @return
     */
    @Override
    public Producto editProducto(Long idProducto,
                                 String nombre,
                                 String marca,
                                 double precio) {

        Producto prod = this.findProducto(idProducto);
        prod.setNombre(nombre);
        prod.setMarca(marca);
        prod.setPrecio(precio);

        this.saveProducto(prod);

        return prod;

    }

    /**
     * Metodo para encontrar un producto mediante su nombre
     * @param nombre
     * @return
     */
    @Override
    public List<Producto> findProductoByNombre(String nombre) {
        List<Producto> producto = repo.findProductoByNombre(nombre);
        return producto;
    }


}
