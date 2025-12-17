package com.todocodeacademy.PruebaTecSupermercado.service.impl;

import com.todocodeacademy.PruebaTecSupermercado.dto.ProductoDTO;
import com.todocodeacademy.PruebaTecSupermercado.exception.NotFoundException;
import com.todocodeacademy.PruebaTecSupermercado.mapper.Mapper;
import com.todocodeacademy.PruebaTecSupermercado.model.Producto;
import com.todocodeacademy.PruebaTecSupermercado.repository.ProductoRepository;
import com.todocodeacademy.PruebaTecSupermercado.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoDTO> traerProductos() {
        return productoRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
         Producto prod= Producto.builder()
                 .nombre(productoDTO.getNombre())
                 .categoria(productoDTO.getCategoria())
                 .precio(productoDTO.getPrecio())
                 .cantidad(productoDTO.getCantidad())
                 .build();
        return Mapper.toDTO(productoRepository.save(prod));
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        //buscar si existe ese producto
        Producto prod=productoRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Producto no encontrado "+id));

        prod.setNombre(productoDTO.getNombre());
        prod.setCategoria(productoDTO.getCategoria());
        prod.setCantidad(productoDTO.getCantidad());
        prod.setPrecio(productoDTO.getPrecio());

        return Mapper.toDTO(productoRepository.save(prod));
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado para eliminar "+id);
        }
        productoRepository.deleteById(id);
    }
}
