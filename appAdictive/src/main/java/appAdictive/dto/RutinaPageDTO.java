package appAdictive.dto;

import appAdictive.util.enums.InfoTipo;
import appAdictive.util.enums.TipoEj;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RutinaPageDTO {
	
	private Integer idRutina;

	private String nombre;
	
	private int copias;

	private InfoTipo descripcionDias;

	private TipoEj tipo;
	
	private String creador;

}
