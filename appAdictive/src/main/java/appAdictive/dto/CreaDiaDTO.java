package appAdictive.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreaDiaDTO {

	@NotNull
	@NotEmpty
	@Valid
	private List<CreaDiaEjercicioDTO> diaEjercicios;
	
	public CreaDiaEjercicioDTO addCDiaEjercicios(CreaDiaEjercicioDTO diaEjercicios) {
		getDiaEjercicios().add(diaEjercicios);
		return diaEjercicios;
	}
}
