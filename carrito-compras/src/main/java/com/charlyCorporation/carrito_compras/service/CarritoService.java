package com.charlyCorporation.carrito_compras.service;

import com.charlyCorporation.carrito_compras.dto.CarritoDTO;
import com.charlyCorporation.carrito_compras.dto.ProductosDTO;
import com.charlyCorporation.carrito_compras.model.Carrito;
import com.charlyCorporation.carrito_compras.repository.ICarritoRepository;
import com.charlyCorporation.carrito_compras.repository.IProductosClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que implementa los metodos de ICarritoService
 */
@Service
public class CarritoService implements ICarritoService {

    /**
     * Inyeccion de dependencias
     */
    @Autowired
    private ICarritoRepository repo;

    /**
     * Inyeccion de dependencia hacia el cliente de Feign
     */
    @Autowired
    private IProductosClient client;

    /**
     * Metodo que permite guardar
     * @param car
     */
    @Override
    public void save(Carrito car) {
        repo.save(car);
    }

    /**
     * Metodo que permite guardar los datos de carrito mediante si Id y un producto
     * @param idCarrito
     * @param listProductos
     */
    @Override
    public void saveCarrito(Long idCarrito,
                            String listProductos) {


        List<String> listProd = new ArrayList<>();
        listProd.add(listProductos);
        List<ProductosDTO> d = client.findByNombre(listProductos);
        Double precio = d.get(0).getPrecio();


        Carrito car = new Carrito();
        car.setIdCarrito(idCarrito);
        car.setPrecioTotal(precio);
        car.setNomProductos(listProd);

        repo.save(car);
    }

    /**
     * Metodo para obtener el listado de todos los productos
     * @return
     */
    @Override
    public List<Carrito> getCarrito() {
        List<Carrito> lista = repo.findAll();
        return lista;
    }

    /**
     * Metodo para encontrar un carrito de compras mediante su Id
     * @param idCarrito
     * @return
     */
    @Override
    public Carrito findById(Long idCarrito) {
        Carrito car = repo.findById(idCarrito).orElse(null);
        return car;
    }


    /**
     * Metodo que permite editar un carrito de comprar agregando un nuevo producto
     * @param idCarrito
     * @param nomProductos
     * @return
     */
    @CircuitBreaker(name = "productos", fallbackMethod = "FallbackAddProdToCarrito")
    @Retry(name = "productos")
    @Override
    public Carrito addProdToCarrito(long idCarrito, String nomProductos) {

        Carrito car = this.findById(idCarrito);
        List<ProductosDTO> lisProd = this.findNombre(nomProductos);
        List<String> nuevosProd = car.getNomProductos();
        Double precioFinal = car.getPrecioTotal() + lisProd.get(0).getPrecio();
        String nuevoNombre = lisProd.get(0).getNombre();
        nuevosProd.add(nuevoNombre);

            car.setIdCarrito(idCarrito);
            car.setPrecioTotal(precioFinal);
            car.setNomProductos(nuevosProd);

            this.save(car);

            return car;

    }


    /**
     * Metodo para econtrar mediante la coneccion de Feign un producto mediante su nombre
     * @param nombre
     * @return
     */
    @CircuitBreaker(name = "productos", fallbackMethod = "FallbackAddProdToCarrito")
    @Retry(name = "productos")
    @Override
    public List<ProductosDTO> findNombre(String nombre) {

        List<ProductosDTO> prodDTO = client.findByNombre(nombre);
        List<ProductosDTO> listProdDTO = new ArrayList<>();

        for (ProductosDTO dto : prodDTO) {
            if (dto.getNombre().contains(nombre)) {
                ProductosDTO prod = new ProductosDTO();
                prod.setIdProducto(dto.getIdProducto());
                prod.setNombre(dto.getNombre());
                prod.setMarca(dto.getMarca());
                prod.setPrecio(dto.getPrecio());

                listProdDTO.add(prod);
            }

        }

        return listProdDTO;
    }

    /**
     * Metodo para eliminar de un carrito de compras un producto
     * @param idCarrito
     * @param nomProductos
     * @return
     */
    @CircuitBreaker(name = "productos", fallbackMethod = "FallbackAddProdToCarrito")
    @Retry(name = "productos")
    @Override
    public Carrito deleteProd(long idCarrito, String nomProductos) {

        Carrito car = this.findById(idCarrito);
        List<ProductosDTO> prodDTO = this.findNombre(nomProductos);
        Double precioFinal = car.getPrecioTotal() - prodDTO.get(0).getPrecio();
        List<String> nuevaList = car.getNomProductos();
        nuevaList.remove(nomProductos);

        car.setIdCarrito(idCarrito);
        car.setPrecioTotal(precioFinal);
        car.setNomProductos(nuevaList);

        this.save(car);

        return car;
    }

    /**
     * Metodo para eliminar un carrito de compras
     * @param idCarrito
     */
    @Override
    public void deleteAllCar(Long idCarrito) {
        repo.deleteById(idCarrito);
    }

    /**
     * Metodo Fallback que permite redirigir el metodo por algun fallo al realizar una peticion
     * @param throwable
     * @return
     */
    public ProductosDTO FallbackAddProdToCarrito(Throwable throwable){
        return new ProductosDTO(99999L, "Error", "Error",00.00);
    }
}


