package appAdictive.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import appAdictive.dto.UsuarioRegistroDTO;
import appAdictive.entity.PesoEntity;
import appAdictive.entity.RutinaEntity;
import appAdictive.entity.Token;
import appAdictive.entity.UsuarioEntity;
import appAdictive.proyection.ValidateLogin;
import appAdictive.repository.PesoRepositorio;
import appAdictive.repository.RutinaRepositorio;
import appAdictive.repository.TokenRepositorio;
import appAdictive.repository.UsuarioRepositorio;
import appAdictive.service.UsuarioService;
import appAdictive.util.Mail;
import appAdictive.util.enums.Role;
import appAdictive.util.exceptions.ConflictException;
import appAdictive.util.exceptions.MyException;
import appAdictive.util.exceptions.NotFoundException;

@Service(value = "usuarioService")
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private RutinaRepositorio rutinaRepositorio;

	@Autowired
	private TokenRepositorio tokenRepositorio;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	private PesoRepositorio pesoRepositorio;
	
	Mail sendEmail = new Mail();

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		ValidateLogin usuarioEntity = usuarioRepositorio.findUserByEmail(email).get();
		if (usuarioEntity == null || !usuarioEntity.isActivo() || usuarioEntity.isBan()) {
			throw new UsernameNotFoundException("Este usuario no tiene permisos para acceder con los valores dados.");
		}
		
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + usuarioEntity.getRole()));

		return new User(usuarioEntity.getEmail(), usuarioEntity.getContrasena(), authorities);
	}

	@Override
	public UsuarioEntity buscaEmail(String email) {
		return usuarioRepositorio.findByEmail(email).get();
	}

	@Override
	public boolean registro(UsuarioRegistroDTO user) throws MyException {
		
		try {
			// Creates Usuario
			UsuarioEntity newUser;

			// Cheks if this email exists in db
			if (usuarioRepositorio.existsById(user.getEmail()))
				throw new MyException("Este email ya existe.");
				
				PesoEntity peso = PesoEntity.builder().peso(user.getPeso()).fecha(new Date()).build();
				
				// Finish the new Rutina this Sunday
		    	Calendar c = Calendar.getInstance();
		    	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

				// Creates the user and saves it
				newUser = UsuarioEntity.builder().nombre(user.getNombre()).email(user.getEmail()).contrasena(bcryptEncoder.encode(user.getContrasena()))
						.activo(false).ban(false).ciclo(4).diasDisponibles(user.getDiasDisponibles()).meta(user.getMeta()).KCal(user.getKCal())
						.rutina(rutinaRepositorio.getOne(new Integer(1))).role(Role.USER).fechaFinal(c.getTime()).build();
		    	
				newUser = usuarioRepositorio.save(newUser);
				peso.setUsuario(newUser);
				pesoRepositorio.save(peso);

			// Creates and saves random token
			Token tokenEntity = generaGuardaToken(newUser);

			// Send confirmation email
			sendEmail.generateAndSendEmail(tokenEntity, null);

		} catch (AddressException e) {
			throw new ConflictException("Hay un problema con el email proporcionado. ", e.getCause());
		} catch (MessagingException e) {
			throw new ConflictException("No se puede tramitar el email. ", e.getCause());
		}
		return true;
	}

	@Override
	public boolean solicitaOlvidaContrasena(String email) throws MyException {
		UsuarioEntity usuario = usuarioRepositorio.findByEmail(email).orElseThrow(() -> 
			new NotFoundException("No se encuentra registrado ese email."));
		
		// Creates random String token
		SecureRandom random = new SecureRandom();
		String nuevaContra = new BigInteger(130, random).toString(30).substring(1, 20);
		usuario.setContrasena(bcryptEncoder.encode(nuevaContra));
		usuarioRepositorio.save(usuario);
		
		// Send confirmation email
		Token fakeToken = new Token();
		fakeToken.setUsuario(usuario);
		try {
			sendEmail.generateAndSendEmail(fakeToken, nuevaContra);
		} catch (AddressException e) {
			throw new ConflictException("Hay un problema con el email proporcionado. ", e.getCause());
		} catch (MessagingException e) {
			throw new ConflictException("No se puede tramitar el email. ", e.getCause());
		}
		return true;
	}

	@Override
	public boolean cambioContrasena(String userEmail, String nuevaContrasena) {
		usuarioRepositorio.updatePass(bcryptEncoder.encode(nuevaContrasena), userEmail);
		return true;
	}

	private Token generaGuardaToken(UsuarioEntity usuario) {

		// Creates random String token
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);

		// Creates TokenEntity and saves it with +24h expiration time
		Token tokenEntity = Token.builder().usuario(usuario).token(token)
				.fecha_expiracion(new Date(System.currentTimeMillis() + 86390000)).build();

		return tokenRepositorio.save(tokenEntity);
	}
	
	@Override
	public boolean ban(String email, boolean borraRutinas) throws MyException {
		UsuarioEntity usuario = usuarioRepositorio.findById(email).orElseThrow(() -> new NotFoundException("El usuario no existe."));
		if (usuario.getRole() == Role.ADMIN)
			throw new ConflictException("No se puede aplicar el baneo a un administrador. ");
			
		usuario.setBan(true);
		usuarioRepositorio.save(usuario);
		
		if (borraRutinas) {
			List<RutinaEntity> rutinasPublicas = rutinaRepositorio.rutinasFromUsuario(email);
			if (!rutinasPublicas.isEmpty()) {
					rutinaRepositorio.deleteInBatch(rutinasPublicas);
			}
		}
		
		return true;
	}

}