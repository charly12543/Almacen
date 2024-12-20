package com.charlyCorporation.ventas.repository;

import com.charlyCorporation.ventas.dto.ProductosDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "productos")
public interface IproductoClient {

    @GetMapping("producto/findByNombre/{nombre}")
    public  List<ProductosDTO> findByNombre(@PathVariable String nombre);


}
