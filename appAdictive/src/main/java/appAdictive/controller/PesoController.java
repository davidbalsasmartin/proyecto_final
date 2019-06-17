package appAdictive.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import appAdictive.dto.PesoDTO;
import appAdictive.service.PesoService;

@Validated
@RestController
@RequestMapping("/peso")
@CrossOrigin(origins = "*", maxAge = 3600)
class PesoController {

	@Autowired
	private PesoService pesoService;

	/**
	 * Devuelve los ultimos 8 pesos por fecha
	 * 
	 * @param principal
	 * @return
	 */
	@GetMapping(path = { "/ultimos" }, produces = "application/json")
	public List<PesoDTO> findLast8ByFecha(Principal principal) {
		return pesoService.buscaPesos(principal.getName());
	}

	/**
	 * Guarda un peso actual siempre que haya 5 dias de diferencia o mas
	 * 
	 * @param principal
	 * @param peso
	 * @return
	 */
	@PostMapping (path = { "/guardar/{peso}" }, produces = "application/json")
	public PesoDTO guardarPeso (Principal principal, @PathVariable @Digits(integer=3,fraction=2) @Min(40) @Max(140) Float peso) {
		
		return pesoService.guardaPeso(peso, principal.getName());
	}
}