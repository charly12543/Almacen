package com.charlyCorporation.ventas.controller;

import com.charlyCorporation.ventas.dto.VentasDTO;
import com.charlyCorporation.ventas.model.Ventas;
import com.charlyCorporation.ventas.service.IVentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase controladora que contien los End-points
 */
@RestController
@RequestMapping("/ventas")
public class VentasController {

    /**
     * Inyeccion de dependencias
     */
    @Autowired
    private IVentasService service;

    /**
     * End-point para guardar una venta
     * @param ventas
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveVenta(@RequestBody Ventas ventas){
        service.saveVenta(ventas);
        return ResponseEntity.ok("Exito en el registro");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Ventas>> listSold(){
        List<Ventas> list = service.listVentas();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/find/{idVenta}")
    public ResponseEntity<VentasDTO> findByIdDTO(@PathVariable Long idVenta){
        VentasDTO ventas = service.findById(idVenta);
        return ResponseEntity.ok(ventas);
    }




}
