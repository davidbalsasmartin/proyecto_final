package appAdictive.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import appAdictive.dto.AutenticacionTokenDTO;
import appAdictive.dto.LoginUserDTO;
import appAdictive.dto.UsuarioRegistroDTO;
import appAdictive.security.TokenProvider;
import appAdictive.service.TokenService;
import appAdictive.service.UsuarioService;
import appAdictive.util.enums.Role;

@Validated
@RestController
@RequestMapping("/enter")
@CrossOrigin(origins = "*", maxAge = 3600)
class LibreController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;
    
    @Autowired
    private UsuarioService userBO;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Registra un nuevo usuario
     * 
     * @param usuario
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value="/register")
    public boolean registro(@RequestBody(required = true) @Valid UsuarioRegistroDTO usuario) {
  
    	return userBO.registro(usuario);
    }
    
    /**
     * Loguea al usuario
     * 
     * @param loginUser
     * @return
     * @throws AuthenticationException
     */
    @PostMapping(value = "/login")
    public ResponseEntity<AutenticacionTokenDTO> login(@RequestBody @Valid LoginUserDTO loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(loginUser.getEmail(),loginUser.getContrasena()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        
        final AutenticacionTokenDTO tokenFront = AutenticacionTokenDTO.builder().token(token).email(authentication.getName())
        		.role(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]") ? Role.ADMIN : Role.USER).build();
        
        return ResponseEntity.ok(tokenFront);
    }
    
    /**
     * Confirma un nuevo usuario a traves de la peticion mandada por email
     * 
     * @param id
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/confirm/{id}/{token}")
    public boolean confirmacion (@PathVariable @Max(30000) @Min(0) Integer id, @PathVariable String token) {
    	return tokenService.confirmacion(id, token);
    }
    
    /**
     * Solicita mandar una nueva contrasena al email
     * 
     * @param email
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/olvida/{email}")
    public boolean olvidaContrasena (@PathVariable @Size(min = 5, max = 50) String email) {
    		return usuarioService.solicitaOlvidaContrasena(email);

    }
}