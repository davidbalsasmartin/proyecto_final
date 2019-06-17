package appAdictive.util;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import appAdictive.entity.Token;
 
 
public class Mail {
 
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
 
	public void generateAndSendEmail(Token token, String nuevaContrasena) throws AddressException, MessagingException {
 
		final String url;
		final String sujeto;
		final String titulo;
		final String cuerpo;
		
		if (nuevaContrasena == null) {
			sujeto = "Email de verificación";
			titulo = ", muchas gracias por haberte suscrito a GYMADE oficial. ";
			url = Constants.FRONT_URL + "/#/login/" + token.getIdToken() + "/" + token.getToken();
			cuerpo = "Por favor, para verificar el email es necesario que pulses " + "<strong> <a href=\" " + url  + " \"> aquí </a> </strong>."; 
		} else {
			sujeto = "Cambio de contraseña";
			titulo = ", ¿Has olvidado la contraseña?";
			cuerpo = "Por favor, vuelve a iniciar sesión con la siguiente contraeña:" +
					"<strong>" + nuevaContrasena + "</strong></p><p>Por favor, inicia sesión y cambia la contraseña lo antes posible.</p>";
		}
		
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
 
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(token.getUsuario().getEmail()));
		generateMailMessage.setSubject(sujeto);
		String emailBody = "<head>" + 
				"<style type=\"text/css\">" + 
				"  .blue { color: #ffea00 }"
				+ "a:link { " + 
				"text-decoration:none;" + 
				"} " + 
				"</style>" + 
				"</head>" + 
				"<h1 class=\"blue\">" + token.getUsuario().getNombre() + titulo + "</h1>" + 
				"<p>" + cuerpo + "</p>"; 
		generateMailMessage.setContent(emailBody, "text/html; charset=utf-8");
 
		
		Transport transport = getMailSession.getTransport("smtp");

		transport.connect("smtp.gmail.com", "gymade.oficial@gmail.com", "gymade333");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}