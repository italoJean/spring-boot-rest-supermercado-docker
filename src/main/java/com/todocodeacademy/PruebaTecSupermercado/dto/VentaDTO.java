package com.todocodeacademy.PruebaTecSupermercado.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {
    private Long id;
    private LocalDate fecha;
    private String estado;
    private Long idSucursal;
    private List<DetalleVentaDTO> detalle;
    private Double total;
}
