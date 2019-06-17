package appAdictive.proyection;

import appAdictive.util.enums.Role;

public interface ValidateLogin {

	String getEmail();

	String getContrasena();

	Role getRole();
	
	boolean isActivo();
	
	boolean isBan();

}