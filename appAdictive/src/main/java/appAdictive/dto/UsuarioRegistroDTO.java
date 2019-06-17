package appAdictive.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import appAdictive.util.enums.TipoEj;
import lombok.Data;

@Data
public class UsuarioRegistroDTO {

	@NotNull
	@NotBlank
	@Size(max = 24)
	private String nombre;

	@NotNull
	@NotBlank
	@Email
	@Size(max = 50, min = 8)
	private String email;

	@NotNull
	@NotBlank
	@Size(min = 6, max = 24)
	private String contrasena;
	
	@NotNull
	@Min(40)
	@Max(160)
	@Digits(fraction = 2, integer = 3)
	private Float peso;
	
	@NotNull
	private TipoEj meta;
	
	@NotNull
	@Digits(fraction = 0, integer = 4)
	@Min(800)
	@Max(4000)
	private int kCal;
	
	@NotNull
	@Min(3)
	@Max(6)
	private int diasDisponibles;

}