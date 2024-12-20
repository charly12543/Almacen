package com.charlyCorporation.ventas.service;

import com.charlyCorporation.ventas.dto.VentasDTO;
import com.charlyCorporation.ventas.model.Ventas;

import java.util.List;

/**
 * Interfaz que contiene los metodos genericos
 */
public interface IVentasService {

    public void saveVenta(Ventas ventas);

    public List<Ventas> listVentas();

    public Ventas find(Long idVenta);

    public VentasDTO findById(Long idVenta);


}
