package com.charlyCorporation.carrito_compras.controller;

import com.charlyCorporation.carrito_compras.dto.CarritoDTO;
import com.charlyCorporation.carrito_compras.dto.ProductosDTO;
import com.charlyCorporation.carrito_compras.model.Carrito;
import com.charlyCorporation.carrito_compras.service.ICarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase controladora que contien los End-points
 */
@RestController
@RequestMapping("/carrito")
public class CarritoController {

    /**
     * Inyeccion de dependencias
     */
    @Autowired
    private ICarritoService service;

    /**
     * Inyectamos el valor del puerto de ejecucion en una variable
     */
    @Value("${server.port}")
    private int serverPort;

    /**
     * End-point para guardar un carrito de compras
     * @param
     * @return
     */
    @PostMapping("/save")
    public String saveCarrito(@RequestBody CarritoDTO d){
        service.saveCarrito(d.getIdCarrito(),d.getNomProductos());
        return "Exito en el guardado";
    }

    /**
     * End-point que muestra la lista de todos los carritos
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Carrito>> listaCarrito(){
        List<Carrito> lista = service.getCarrito();
        return ResponseEntity.ok(lista);
    }

    /**
     * End-point para encontraer un producto mediante su nombre
     * @param nombre
     * @return
     */
    @GetMapping("/findByNombre/{nombre}")
    public List<ProductosDTO> findByNombre(@PathVariable String nombre ){
        List<ProductosDTO> list = service.findNombre(nombre);
        return list;
    }

    /**
     * End-point para encontrar un carrito mediante su Id
     * @param idCarrito
     * @return
     */
    @GetMapping("/find/{idCarrito}")
    public Carrito find(@PathVariable Long idCarrito){
        Carrito car = service.findById(idCarrito);
        return car;
    }

    /**
     * End-point para agregar un producto mediante su nombre a un carrito de compras
     * @param idCarrito
     * @param nomProductos
     * @return
     */
    @PutMapping("/add/{idCarrito}")
    public Carrito  addProdToCar(@PathVariable long idCarrito,
                                 @RequestParam(required = false, name = "nombre")
                                 String nomProductos){
        Carrito dto = service.addProdToCarrito(idCarrito,nomProductos);
        System.out.println("Probando el balanceador " + serverPort);
        return dto;
    }

    /**
     * End-point para eliminar un producto mediante su nombre a un carrito de compras
     * @param idCarrito
     * @param nomProductos
     * @return
     */
    @PutMapping("/remove/{idCarrito}")
    public Carrito removeProdToCar(@PathVariable long idCarrito,
                                 @RequestParam(required = false, name = "nombre")
                                 String nomProductos){
        Carrito dto = service.deleteProd(idCarrito,nomProductos);
        return dto;
    }

    /**
     * End-point para eliminar el carrito de compras mediante su Id
     * @param idCarrito
     * @return
     */
    @GetMapping("/delete/{idCarrito}")
    public String deleteCar(@PathVariable Long idCarrito){
        service.deleteAllCar(idCarrito);
        return "Eliminado con exito";
    }


}
