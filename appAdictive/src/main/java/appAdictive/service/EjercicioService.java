package appAdictive.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import appAdictive.dto.EjercicioDTO;
import appAdictive.entity.EjercicioEntity;
import appAdictive.util.exceptions.MyException;

public interface EjercicioService {
	
	public EjercicioDTO buscaEjercicio (Integer id) throws MyException;
	
	public List<EjercicioDTO> buscaEjercicios();
	
	public Page<EjercicioEntity> ejerciciosPage(Pageable Page);
}
