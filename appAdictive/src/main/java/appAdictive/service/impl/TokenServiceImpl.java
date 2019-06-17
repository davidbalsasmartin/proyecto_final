package appAdictive.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import appAdictive.entity.Token;
import appAdictive.repository.TokenRepositorio;
import appAdictive.repository.UsuarioRepositorio;
import appAdictive.service.TokenService;
import appAdictive.util.Mail;
import appAdictive.util.exceptions.ConflictException;
import appAdictive.util.exceptions.MyException;

@Service(value = "tokenService")
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private TokenRepositorio tokenRepositorio;
	
	Mail sendEmail = new Mail();
	
	@Override
	public boolean confirmacion (Integer id, String token) throws MyException {
	
		try {
			Optional<Token> tokenOpt = tokenRepositorio.findById(id);
			if (tokenOpt.isPresent() && (tokenOpt.get().getToken().equals(token)) && (tokenOpt.get().getFecha_expiracion().compareTo(
					new Date(System.currentTimeMillis())) >= 0) && !tokenOpt.get().getUsuario().isBan()) {
				tokenOpt.get().getUsuario().setActivo(true);
		
				usuarioRepositorio.save(tokenOpt.get().getUsuario());
				tokenRepositorio.delete(tokenOpt.get());
				return true;
			} else if (tokenOpt.isPresent() && (tokenOpt.get().getToken().equals(token)) && (tokenOpt.get().getFecha_expiracion().compareTo(
					new Date(System.currentTimeMillis())) < 0)) {
				
				// Creates and saves random token
				Token tokenEntity = generaActualizaToken(tokenOpt.get());
		
				// Send confirmation email
				sendEmail.generateAndSendEmail(tokenEntity, null);
				
				return false;
			} else {
				return false;
			}
		} catch (MessagingException e) {
			throw new ConflictException(e.getMessage());
		}
	}

	private Token generaActualizaToken(Token tokenEntity) {

		// Creates random String token
		SecureRandom random = new SecureRandom();
		// Updates TokenEntity and saves it with +24h expiration time
		tokenEntity.setToken(new BigInteger(130, random).toString(32));
		tokenEntity.setFecha_expiracion(new Date(System.currentTimeMillis() + 86390000));

		return tokenRepositorio.save(tokenEntity);
	}

}