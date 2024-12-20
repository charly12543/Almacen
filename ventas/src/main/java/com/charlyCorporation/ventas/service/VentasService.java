package com.charlyCorporation.ventas.service;

import com.charlyCorporation.ventas.dto.CarritoDTO;
import com.charlyCorporation.ventas.dto.ProductosDTO;
import com.charlyCorporation.ventas.dto.VentasDTO;
import com.charlyCorporation.ventas.model.Ventas;
import com.charlyCorporation.ventas.repository.ICarritoClient;
import com.charlyCorporation.ventas.repository.IVentasRepository;
import com.charlyCorporation.ventas.repository.IproductoClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa los metodos de la interfaz IVentasService
 */
@Service
public class VentasService implements IVentasService{

    /**
     * Inyeccion de dependecias
     */
    @Autowired
    private IVentasRepository repo;

    /**
     * Inyeccion de dependencia para el consumo del cliente feign de la clase Carrito
     */
    @Autowired
    private ICarritoClient carritoClient;

    @Autowired
    private IproductoClient prodClient;

    @Override
    public void saveVenta(Ventas ventas) {
        repo.save(ventas);
    }

    @Override
    public List<Ventas> listVentas() {
        List<Ventas> list = repo.findAll();
        return list;
    }

    @Override
    public Ventas find(Long idVenta) {
        Ventas venta = repo.findById(idVenta).orElse(null);
        return venta;
    }

    @CircuitBreaker(name = "productos", fallbackMethod = "fallbackfindById")
    @Retry(name = "productos")
    @Override
    public VentasDTO findById(Long idVenta) {

        Ventas venta = this.find(idVenta);
        CarritoDTO carritoDTO = carritoClient.find(venta.getIdCarrito());
        List<String> listNomProd = carritoDTO.getNomProductos();
        List<ProductosDTO> newList = new ArrayList<>();

        for (String nom : listNomProd){
            List<ProductosDTO> listProducts = prodClient.findByNombre(nom);
            for(ProductosDTO dto : listProducts) {
                if (dto.getNombre().contains(nom)) {

                    ProductosDTO prod = new ProductosDTO();
                    prod.setIdProducto(dto.getIdProducto());
                    prod.setNombre(dto.getNombre());
                    prod.setMarca(dto.getMarca());
                    prod.setPrecio(dto.getPrecio());

                    newList.add(prod);

                }
            }
        }

        VentasDTO ventasDTO = new VentasDTO();
        ventasDTO.setIdVenta(venta.getIdVenta());
        ventasDTO.setFechaVenta(venta.getFechaVenta());
        ventasDTO.setIdCarrito(carritoDTO.getIdCarrito());
        ventasDTO.setVentaTotal(carritoDTO.getPrecioTotal());
        ventasDTO.setListProductos(newList);

        //creatException();

        return ventasDTO;
    }



    /**
     * Metodo Fallback que permite redirigir el metodo por algun fallo al realizar una peticion
     * @param throwable
     * @return
     */
    public VentasDTO fallbackfindById(Throwable throwable){
        //return new ProductosDTO(99999L, "Error", "Error",00.00);
        return new VentasDTO(9999L,null,99999L,0.0,null);
    }

    public void creatException(){
        throw  new IllegalArgumentException("Prueba de circuitBreaker");
    }
}
