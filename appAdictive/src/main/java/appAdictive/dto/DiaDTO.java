package appAdictive.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DiaDTO {

	@NotNull
	@Digits(fraction = 0, integer = 8)
	@Min(0)
	private Integer idDia;

	@NotNull
	@NotEmpty
	@Valid
	private List<DiaEjercicioDTO> diaEjercicios;
	
	public DiaEjercicioDTO addDiaEjercicios(DiaEjercicioDTO diaEjercicios) {
		getDiaEjercicios().add(diaEjercicios);
		return diaEjercicios;
	}
}
