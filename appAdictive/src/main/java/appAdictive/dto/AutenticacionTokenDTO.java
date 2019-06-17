package appAdictive.dto;

import appAdictive.util.enums.Role;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * Token para el login
 *
 */
@Data
@Builder
public class AutenticacionTokenDTO {

	private String token;

	private Role role;
	
	private String email;

}
