package appAdictive.controller;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import appAdictive.dto.EjercicioDTO;
import appAdictive.entity.EjercicioEntity;
import appAdictive.service.EjercicioService;

@Validated
@RestController
@RequestMapping("/ejercicio")
@CrossOrigin(origins = "*", maxAge = 3600)
class EjercicioController {

	@Autowired
	private EjercicioService ejercicioService;
	
	/**
	 * Busca un ejercicio por su id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(path = { "/busca/{id}" }, produces = "application/json")
	public EjercicioDTO findById (@PathVariable @Digits(fraction = 0, integer = 8) Integer id) {
		return ejercicioService.buscaEjercicio(id);
	}

	/**
	 * Busca todos los ejercicios
	 * 
	 * @return
	 */
	@GetMapping(path = { "/todos" }, produces = "application/json")
	public List<EjercicioDTO> findAll() {
		return ejercicioService.buscaEjercicios();
	}
	
	/**
	 * Devuelve todos los ejercicios paginados
	 * 
	 * @param nPage
	 * @return
	 */
	@PostMapping(path = { "/todospage" }, produces = "application/json")
	public Page<EjercicioEntity> ejerciciosPage (@RequestParam("page") @Min(0) @Max(10000) int nPage) {
		Pageable page = PageRequest.of(nPage, 8, Sort.by(Direction.ASC, "nombre"));
		
		return ejercicioService.ejerciciosPage(page);
	}
}