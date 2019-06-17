package appAdictive.dto;

import java.util.Date;
import java.util.List;

import appAdictive.util.enums.InfoTipo;
import appAdictive.util.enums.TipoEj;
import lombok.Data;

@Data
public class RutinaPersDTO {

	private String nombre;

	private InfoTipo descripcionDias;
	
	private Date fechaFinal;

	private TipoEj tipo;
	
	private int kcal;
	
	private int numeroDias;
	
	private List<DiaDTO> dias;
	
	public DiaDTO addDia(DiaDTO dias) {
		getDias().add(dias);
		return dias;
	}

}
