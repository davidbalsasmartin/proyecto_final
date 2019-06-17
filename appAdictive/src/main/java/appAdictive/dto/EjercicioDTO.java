package appAdictive.dto;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import appAdictive.util.enums.Requisito;
import appAdictive.util.enums.TipoEjercicio;
import lombok.Data;

@Data
public class EjercicioDTO {
	
	@NotNull
	@Digits(fraction = 0, integer = 8)
	@Min(1)
	private Integer idEjercicio;

	private String nombre;

	private Requisito requisito;

	private TipoEjercicio tipo;
	
	private List<MusculoDTO> musculosPrinc;

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