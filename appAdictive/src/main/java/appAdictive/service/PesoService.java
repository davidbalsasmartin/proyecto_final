package appAdictive.service;

import java.util.List;

import appAdictive.dto.PesoDTO;
import appAdictive.util.exceptions.MyException;

public interface PesoService {
	
	public List<PesoDTO> buscaPesos (String email);
	
	public PesoDTO guardaPeso (Float pesoDTO, String email) throws MyException;
}
