package com.charlyCorporation.ventas.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Clase POJO contiene las propiedades de las Ventas
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ventas {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaVenta;
    private Long idCarrito;

}
