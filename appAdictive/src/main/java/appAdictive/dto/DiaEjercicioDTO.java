package appAdictive.dto;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class DiaEjercicioDTO {

	@NotNull
	@Digits(fraction = 0, integer = 8)
	@Min(0)
	private Integer idDiaEjercicio;

	@NotNull
	@Pattern(regexp = "[1-9]([0-9])?([-][1-9]([0-9])?)?x[1-9]([0-9])?([-][1-9]([0-9])?)?")
	private String series;
	
	@Min(0)
	@Max(300)
	private int descanso;

	@NotNull
	@Min(20)
	@Max(100)
	private int intensidad;
 
    @NotNull
    @Valid
    @NotEmpty
    private EjercicioDTO ejercicio;

}
