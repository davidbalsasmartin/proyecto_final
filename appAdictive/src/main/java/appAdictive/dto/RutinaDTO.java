package appAdictive.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import appAdictive.util.enums.InfoTipo;
import lombok.Data;

@Data
public class RutinaDTO {

	@NotNull
	@Digits(fraction = 0, integer = 8)
	@Min(0)
	private Integer idRutina;

	@NotNull
	@NotBlank
	@Size(min = 6, max = 30)
	private String nombre;
	
	private int copias;
	
	private Date fechaSubida;

	private InfoTipo descripcionDias;

	@Size(max = 120)
	private String descripcion;

	private String tipo;
	
	private String idUsuario;
	
	private String creador;

	@Valid
	private List<DiaDTO> dias;
	
	public DiaDTO addDia(DiaDTO dias) {
		getDias().add(dias);
		return dias;
	}

}
