package com.charlyCorporation.productos.controller;

import com.charlyCorporation.productos.model.Producto;
import com.charlyCorporation.productos.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Clase controladora que contiene los End-points
 */
@RestController
@RequestMapping("/producto")
public class ProductoController {

    /**
     * Inyeccion de Dependencias
     */
    @Autowired
    private IProductoService service;

    /**
     * Inyectamos el valor del puerto de ejecucion en una variable
     */
    @Value("${server.port}")
    private int serverPort;

    /**
     * Metodo para agregar un producto
     * @param prod
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<Producto> saveProducto(@RequestBody Producto prod){
        service.saveProducto(prod);
        return ResponseEntity.ok(prod);
    }

    /**
     * Controlador para obtener el listado de los productos
     * @return
     */
    @GetMapping("/listProductos")
    private ResponseEntity<List<Producto>> getProductos(){
      List<Producto> lista = service.getProductos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Controlador para encontrar un producto por medio de su Id
     * @param idProducto
     * @return
     */
    @GetMapping("/find/{idProducto}")
    public Producto findById(@PathVariable Long idProducto){
        Producto prod = service.findProducto(idProducto);
        return prod;

    }

    /**
     * Controlador para encontrar un producto por medio de su nombre
     * @param nombre
     * @return
     */
    @GetMapping("/findByNombre/{nombre}")
    public  List<Producto> findByNombre(@PathVariable String nombre){
        List<Producto> producto = service.findProductoByNombre(nombre);
        //Mensaje para probar el puerto donde se esta ejecutando el balanceador de carga
        System.out.println("Probando el balanceador: estoy en el puerto = " + serverPort);
        return producto;
    }

    /**
     * Controlador para eliminar un producto por medio de su Id
     * @param idProducto
     * @return
     */
    @DeleteMapping("/delete/{idProducto}")
    public ResponseEntity<String> deleteProd(@PathVariable Long idProducto){
        service.deleteProducto(idProducto);
        return ResponseEntity.ok("Exito en la eliminacion del producto");
    }

    @GetMapping("/no-seguro")
    public String noSeguro(){
        return "Mensaje NO seguro";
    }

    @GetMapping("/seguro")
    public String seguro(){
        return "Mensaje SI seguro";
    }


}
