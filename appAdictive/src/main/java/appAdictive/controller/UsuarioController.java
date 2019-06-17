package appAdictive.controller;

import java.security.Principal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import appAdictive.service.RutinaService;
import appAdictive.service.UsuarioService;
import appAdictive.util.enums.TipoEj;

@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private RutinaService rutinaService;

	/**
	 * 
	 * 
	 * @param email
	 * @param borraRutinas
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(rollbackFor = Exception.class)
	@PostMapping (path = { "/ban/{email}/{borraRutinas}"})
	public boolean ban (@PathVariable @Size(min = 5, max = 50) String email, @PathVariable boolean borraRutinas) {
			return usuarioService.ban(email, borraRutinas);
	}
	
	/**
	 * actualiza las preferencias de un usuario
	 * 
	 * @param diasDisponibles
	 * @param meta
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@Transactional(rollbackFor = Exception.class)
	@PostMapping(path = { "/updateUser/{diasDisponibles}/{meta}"})
	public boolean updateUser (@PathVariable @Min(3) @Max(6) int diasDisponibles,@PathVariable @Size(min = 2, max = 15)String meta, Principal principal) {
		TipoEj nuevaMeta = TipoEj.fromValue(meta);
		return rutinaService.updateUser(principal.getName(), diasDisponibles, nuevaMeta);
	}
	
	/**
	 * cambia la contrasena del usuario
	 * 
	 * @param principal
	 * @param contrasena
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
    @PostMapping(value="/modifica/{contrasena}")
    public ResponseEntity<Boolean> cambioContrasena(Principal principal, @PathVariable @Size(min = 6, max = 24) String contrasena) {
    	
    	return ResponseEntity.ok(usuarioService.cambioContrasena(principal.getName(), contrasena));
    }
}