package appAdictive.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreaEjercicioDTO {
	
	@NotNull
	@NotBlank
	private String nombre;

	@NotNull
	@NotBlank
	private String requisito;

	@NotNull
	@NotBlank
	private String tipo;
	
	@NotNull
	@Valid
	@NotEmpty
	private List<MusculoDTO> musculosPrinc;

	@Valid
	private List<MusculoDTO> musculosSec;
	
	public MusculoDTO addMusculosPrinc(MusculoDTO musculoDTO) {
		getMusculosPrinc().add(musculoDTO);
		return musculoDTO;
	}
	
	public MusculoDTO addMusculosSec(MusculoDTO musculoDTO) {
		getMusculosSec().add(musculoDTO);
		return musculoDTO;
	}

}