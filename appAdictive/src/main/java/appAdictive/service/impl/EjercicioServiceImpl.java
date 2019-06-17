package appAdictive.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import appAdictive.dto.EjercicioDTO;
import appAdictive.entity.EjercicioEntity;
import appAdictive.repository.EjercicioRepositorio;
import appAdictive.service.EjercicioService;
import appAdictive.util.exceptions.MyException;
import appAdictive.util.exceptions.NotFoundException;

@Service
public class EjercicioServiceImpl implements EjercicioService {
 
    @Autowired
    private EjercicioRepositorio ejercicioRepositorio;
    
    public EjercicioServiceImpl(EjercicioRepositorio ejercicioRepositorio) {
		this.ejercicioRepositorio = ejercicioRepositorio;
	}
    
    @Override
    public EjercicioDTO buscaEjercicio (Integer id) throws MyException {
    	EjercicioEntity ejercicioEntity = ejercicioRepositorio.findById(id).orElseThrow(() -> 
    	new NotFoundException("Ejercicio no encontrado."));

    		ModelMapper modelMapper = new ModelMapper();
    		EjercicioDTO ejercicioDTO = modelMapper.map(ejercicioEntity, EjercicioDTO.class);

    	return ejercicioDTO;
    }
    
    @Override
    public List<EjercicioDTO> buscaEjercicios () {
    	List<EjercicioEntity> ejerciciosEntity = ejercicioRepositorio.findAllByOrderByNombre();
    	
    	ModelMapper modelMapper = new ModelMapper();
    	List<EjercicioDTO> ejerciciosDTO = new ArrayList<>();
    	
    	ejerciciosEntity.forEach(ejercicioEntity ->
    		ejerciciosDTO.add(modelMapper.map(ejercicioEntity, EjercicioDTO.class))
    	);
    	
    	return ejerciciosDTO;
    }
    
    @Override
    public Page<EjercicioEntity> ejerciciosPage (Pageable page) {
    	
    	return ejercicioRepositorio.findAll(page);
    } 
}