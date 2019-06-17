package appAdictive.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import appAdictive.dto.CreaRutinaDTO;
import appAdictive.dto.Estadisticas;
import appAdictive.dto.RutinaDTO;
import appAdictive.dto.RutinaPageDTO;
import appAdictive.dto.RutinaPersDTO;
import appAdictive.dto.RutinaSearch;
import appAdictive.util.enums.TipoEj;
import appAdictive.util.exceptions.MyException;

public interface RutinaService {
	
	public RutinaDTO buscaRutina(String email, Integer id) throws MyException;
	
	public boolean creaRutina(CreaRutinaDTO rutinaDTO, String email) throws MyException;
	
	public boolean publicar(Integer id, String email) throws MyException;
	
	public boolean oficial(Integer id) throws MyException;
	
	public boolean creaRutinaAdmin(CreaRutinaDTO rutinaDTO) throws MyException;
	
	public boolean asignarRutinaManual (Integer idRutina, String email) throws MyException;
	
	public boolean elimina (Integer id, String email) throws MyException;
		
	public boolean modificaRutina(RutinaDTO rutinaDTO, String email) throws MyException;
	
	public boolean copiar(Integer id, String email) throws MyException;
	
	public List<RutinaSearch> buscaNombre(String nombre);
	
	public RutinaPersDTO home (String email) throws MyException;
	
	public Page<RutinaPageDTO> misRutinasPage(String email, Pageable page);
	
	public Page<RutinaPageDTO> rutinasPublicasPage(Pageable page, boolean oficial);
	
	public boolean updateUser(String email, int diasDisponibles, TipoEj meta) throws MyException;
	
	public boolean asignaRutinaAutoFromServer (String email) throws MyException;
	
	public Estadisticas obtieneEstadistica();
}