package appAdictive.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import appAdictive.util.enums.InfoTipo;
import appAdictive.util.enums.TipoEj;
import lombok.Data;

@Data
public class CreaRutinaDTO {

	@NotNull
	@NotEmpty
	@Size(max = 30)
	private String nombre;

	@NotNull
	private InfoTipo descripcionDias;

	@Size(max = 120)
	private String descripcion;

	@NotNull
	private TipoEj tipo;

	@NotNull
	@NotEmpty
	@Valid
	private List<CreaDiaDTO> dias;
	
	public CreaDiaDTO addDia(CreaDiaDTO dias) {
		getDias().add(dias);
		return dias;
	}

}
