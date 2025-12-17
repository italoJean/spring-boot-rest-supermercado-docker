package com.todocodeacademy.PruebaTecSupermercado.service.impl;

import com.todocodeacademy.PruebaTecSupermercado.dto.SucursalDTO;
import com.todocodeacademy.PruebaTecSupermercado.exception.NotFoundException;
import com.todocodeacademy.PruebaTecSupermercado.mapper.Mapper;
import com.todocodeacademy.PruebaTecSupermercado.model.Sucursal;
import com.todocodeacademy.PruebaTecSupermercado.repository.SucursalRepository;
import com.todocodeacademy.PruebaTecSupermercado.service.ISucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService implements ISucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<SucursalDTO> traerSucursales() {
        return sucursalRepository.findAll()
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public SucursalDTO crearSucursal(SucursalDTO sucursalDTO) {
        Sucursal sucursal=Sucursal.builder()
                .nombre(sucursalDTO.getNombre())
                .direccion(sucursalDTO.getDireccion())
                .build();
        return Mapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    public SucursalDTO actualizarSucursal(Long id, SucursalDTO sucursalDTO) {
        Sucursal sucursal=sucursalRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Sucursal no encontrado con id "+id));

        sucursal.setNombre(sucursalDTO.getNombre());
        sucursal.setDireccion(sucursalDTO.getDireccion());

        return Mapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    public void eliminarSucursal(Long id) {
        if (!sucursalRepository.existsById(id)){
            throw new NotFoundException("Sucursal no encontrada para eliminar "+id);
        }
        sucursalRepository.deleteById(id);
    }
}
