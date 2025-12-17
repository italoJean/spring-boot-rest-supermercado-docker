package com.todocodeacademy.PruebaTecSupermercado.service.impl;

import com.todocodeacademy.PruebaTecSupermercado.dto.DetalleVentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.dto.VentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.exception.NotFoundException;
import com.todocodeacademy.PruebaTecSupermercado.mapper.Mapper;
import com.todocodeacademy.PruebaTecSupermercado.model.DetalleVenta;
import com.todocodeacademy.PruebaTecSupermercado.model.Producto;
import com.todocodeacademy.PruebaTecSupermercado.model.Sucursal;
import com.todocodeacademy.PruebaTecSupermercado.model.Venta;
import com.todocodeacademy.PruebaTecSupermercado.repository.ProductoRepository;
import com.todocodeacademy.PruebaTecSupermercado.repository.SucursalRepository;
import com.todocodeacademy.PruebaTecSupermercado.repository.VentaRepository;
import com.todocodeacademy.PruebaTecSupermercado.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<VentaDTO> traerVentas() {
        List<Venta> ventas= ventaRepository.findAll();
        List<VentaDTO> ventasDTO=new ArrayList<>();

        VentaDTO dto;
        for (Venta v: ventas){
             dto=Mapper.toDTO(v);
            ventasDTO.add(dto);
        }
        return ventasDTO;
    }

    @Override
    public VentaDTO crearVenta(VentaDTO ventaDTO) {

        //Validaciones
        if (ventaDTO==null) throw new RuntimeException("VentaDTO es null");
        if (ventaDTO.getIdSucursal()==null) throw new RuntimeException("Debe indicar la sucursal");
        if (ventaDTO.getDetalle()==null || ventaDTO.getDetalle().isEmpty())
            throw new RuntimeException("Debe incluir al menos un producto");

        //Buscar la sucursal
        Sucursal sucursal=sucursalRepository.findById(ventaDTO.getIdSucursal()).orElse(null);
        if (sucursal==null){
            throw new NotFoundException("Sucursal no encontrado" );
        }

        //Crear la venta
        Venta venta=new Venta();
        venta.setFecha(ventaDTO.getFecha());
        venta.setEstado(ventaDTO.getEstado());
        venta.setSucursal(sucursal);
        venta.setTotal(ventaDTO.getTotal());

        //La lista de detalles
        // --> Aca estan los productos
        List<DetalleVenta> detalles=new ArrayList<>();
//    Double totalCalculado=0.0;

        for (DetalleVentaDTO detalleVentaDTO: ventaDTO.getDetalle()){
            // Buscar producto por id (tu detalleDTO usa id como id de producto)
            Producto producto= productoRepository.findByNombre(detalleVentaDTO.getNombreProd()).orElse(null);
            if (producto==null){
                throw new RuntimeException("Producto no encontrado con el nombre: "+detalleVentaDTO.getNombreProd());
            }

            // Crear Detalle
            DetalleVenta detalleVenta=new DetalleVenta();
            detalleVenta.setProd(producto);
            detalleVenta.setPrecio(detalleVentaDTO.getPrecio());
            detalleVenta.setCantProd(detalleVentaDTO.getCantProd());
            detalleVenta.setVenta(venta);

            detalles.add(detalleVenta);
//            totalCalculado=totalCalculado+(detalleVentaDTO.getPrecio()*detalleVentaDTO.getCantProd());
        }

        //Seteamos la lista de detalle venta
        venta.setDetalle(detalles);

        //guardamos en bd
        venta=ventaRepository.save(venta);

        //Mapeo de salida
        VentaDTO ventaSalida=Mapper.toDTO(venta);

        return ventaSalida;
    }

    @Override
    public VentaDTO actualizarVenta(Long id, VentaDTO ventaDTO) {
        //Buscar si la venta existe para actualizarla
        Venta v=ventaRepository.findById(id).orElse(null);
        if (v==null) throw new NotFoundException("Venta no encontrada con id "+id);

        if (ventaDTO.getFecha()!=null){
            v.setFecha(ventaDTO.getFecha());
        }
        if (ventaDTO.getEstado()!=null){
            v.setEstado(ventaDTO.getEstado());
        }
        if (ventaDTO.getTotal()!=null){
            v.setTotal(ventaDTO.getTotal());
        }
        if (ventaDTO.getIdSucursal()!=null){
            Sucursal sucursal=sucursalRepository.findById(ventaDTO.getIdSucursal()).orElse(null);
            if (sucursal==null) throw new NotFoundException("Sucursal no encontrada");
            v.setSucursal(sucursal);
        }
        ventaRepository.save(v);

        VentaDTO ventaSalida=Mapper.toDTO(v);
        return ventaSalida;
    }

    @Override
    public void eliminarVenta(Long id) {
        Venta v=ventaRepository.findById(id).orElse(null);
        if (v==null) throw new NotFoundException("Venta no encontrada con id "+id);
        ventaRepository.delete(v);
    }
}
