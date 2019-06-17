package appAdictive.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import appAdictive.dto.CreaRutinaDTO;
import appAdictive.dto.Estadisticas;
import appAdictive.dto.RutinaDTO;
import appAdictive.dto.RutinaPageDTO;
import appAdictive.dto.RutinaPersDTO;
import appAdictive.dto.RutinaSearch;
import appAdictive.service.RutinaService;

@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/rutina")
class RutinaController {

	@Autowired
	private RutinaService rutinaService;

	/**
	 * Devuelve un listado paginado de 10 en 10 de las rutinas del usuario ordenadas por nombre
	 * 
	 * @param nPage
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = { "/misrutinaspage" }, produces = "application/json")
	public Page<RutinaPageDTO> misRutinasPage(@RequestParam("page") @Min(0) @Max(10000) int nPage,
			Principal principal) {
		Pageable page = PageRequest.of(nPage, 10, Sort.by(Direction.ASC, "nombre"));
		return rutinaService.misRutinasPage(principal.getName(), page);
	}

	/**
	 * Devuelve un listado paginado de 10 en 10 de las rutinas publicas ordenadas por nombre
	 * 
	 * @param nPage
	 * @param principal
	 * @return
	 */
	@PostMapping(path = { "/rutinaspublicas" }, produces = "application/json")
	public Page<RutinaPageDTO> rutinasPublicasPage(@RequestParam("page") @Min(0) @Max(10000) int nPage,
			@RequestParam("oficial") boolean oficial) {
		Pageable page = PageRequest.of(nPage, 10, Sort.by(Direction.ASC, oficial ? "nombre" : "fechaCreacion"));
		return rutinaService.rutinasPublicasPage(page, oficial);
	}

	/**
	 * borra una rutina publica si es admin o del usuario que sea si es user
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@DeleteMapping(path = { "/elimina/{id}" }, produces = "application/json")
	public boolean borraRutina(@PathVariable("id") @Digits(fraction = 0, integer = 8) Integer id, Principal principal) {
		boolean respuesta = false;
		respuesta = rutinaService.elimina(id, principal.getName());

		return respuesta;
	}

	/**
	 * busca una rutina por su id
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@GetMapping(path = { "/busca/{id}" }, produces = "application/json")
	public RutinaDTO buscaRutina(@PathVariable @Digits(fraction = 0, integer = 8) Integer id, Principal principal) {
		return rutinaService.buscaRutina(principal.getName(), id);
	}

	/**
	 * Asigna una rutina manualmente a un usuario
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = { "/asigna/{id}" })
	public boolean asignarRutinaManual(@PathVariable @Digits(fraction = 0, integer = 8) Integer id, Principal principal) {
		return rutinaService.asignarRutinaManual(id, principal.getName());
	}

	/**
	 * Crea una rutina para el usuario
	 * 
	 * @param rutinaDTO
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = { "/creaRutina" })
	public boolean creaRutinaUser(@RequestBody(required = true) @Valid CreaRutinaDTO rutinaDTO, Principal principal) {
		return rutinaService.creaRutina(rutinaDTO, principal.getName());
	}

	/**
	 * Crea una rutina oficial el administrador
	 * 
	 * @param rutinaDTO
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(path = { "/creaRutinaAdmin" })
	public boolean creaRutinaAdmin(@RequestBody(required = true) @Valid CreaRutinaDTO rutinaDTO, Principal principal) {
		return rutinaService.creaRutinaAdmin(rutinaDTO);
	}

	/**
	 * Publica una de sus rutinas el usuario
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/publica/{id}")
	public boolean publicar(@PathVariable @Digits(fraction = 0, integer = 8) Integer id, Principal principal) {
		return rutinaService.publicar(id, principal.getName());

	}

	/**
	 * Hace oficial una rutina publica el administrador
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(path = { "/oficial/{id}" })
	public boolean oficial(@PathVariable @Digits(fraction = 0, integer = 8) Integer id) {
		return rutinaService.oficial(id);
	}

	/**
	 * modifica la rutina
	 * 
	 * @param rutinaDTO
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/modifica")
	public boolean modifica(@RequestBody(required = true) @Valid RutinaDTO rutinaDTO, Principal principal) {

		return rutinaService.modificaRutina(rutinaDTO, principal.getName());

	}

	/**
	 * copia una rutina
	 * 
	 * @param id
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(value = "/copia/{id}")
	public boolean copiar(@PathVariable @Digits(fraction = 0, integer = 8) Integer id, Principal principal) {
		return rutinaService.copiar(id, principal.getName());
	}

	/**
	 * busca una/s rutina/s por nombre 
	 * 
	 * @param nombre
	 * @return
	 */
	@GetMapping(path = { "/buscanombre/{nombre}" })
	public List<RutinaSearch> buscaNombre(@PathVariable @Size(max = 30) String nombre) {
		return rutinaService.buscaNombre(nombre);
	}

	/**
	 * Devuelve la informacion semanal del usuario de su rutina
	 * 
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = { "/home" })
	public RutinaPersDTO home(Principal principal) {
		return rutinaService.home(principal.getName());
	}

	/**
	 * asigna una rutina automaticamente al usuario
	 * 
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = { "/asignaauto" })
	public boolean asignaRutinaAuto(Principal principal) {
		return rutinaService.asignaRutinaAutoFromServer(principal.getName());
	}

	/**
	 * obtiene estadisticas de rutinas y usuarios el administrador
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(path = { "/estadistica" })
	public Estadisticas obtieneEstadistica() {
		return rutinaService.obtieneEstadistica();
	}
}