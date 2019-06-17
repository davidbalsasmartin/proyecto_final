package appAdictive.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Validated
@Data 
public class LoginUserDTO {

	@NotNull
	@Email
	@NotBlank
    private String email;
    
	@NotNull
	@NotBlank
	@Size(min = 6, max = 24)
    private String contrasena;
    
}
