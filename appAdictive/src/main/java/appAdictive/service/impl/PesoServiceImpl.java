package appAdictive.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import appAdictive.dto.PesoDTO;
import appAdictive.entity.PesoEntity;
import appAdictive.entity.UsuarioEntity;
import appAdictive.repository.PesoRepositorio;
import appAdictive.repository.UsuarioRepositorio;
import appAdictive.service.PesoService;
import appAdictive.util.enums.TipoEj;
import appAdictive.util.exceptions.ConflictException;
import appAdictive.util.exceptions.MyException;

@Service
public class PesoServiceImpl implements PesoService {

	@Autowired
	private PesoRepositorio pesoRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Override
	public List<PesoDTO> buscaPesos (String email) {

		UsuarioEntity usuarioEntity = usuarioRepositorio.getOne(email);
		
		List<PesoEntity> pesosEntities = pesoRepositorio.findFirst8ByUsuarioOrderByFechaDesc(usuarioEntity);
		Collections.reverse(pesosEntities);
		
		ModelMapper modelMapper = new ModelMapper();
		List<PesoDTO> pesosDTO = new ArrayList<>();
		pesosEntities.forEach(pesoEntity ->pesosDTO.add(modelMapper.map(pesoEntity, PesoDTO.class)));
		
		return pesosDTO;
	}
	
	@Override
	public PesoDTO guardaPeso (Float peso, String email) throws MyException {
		PesoEntity pesoEntity = pesoRepositorio.findFirstByUsuarioOrderByFechaDesc(usuarioRepositorio.getOne(email));
		int diferenciaDias = (int)TimeUnit.DAYS.convert((new Date().getTime() - pesoEntity.getFecha().getTime()), TimeUnit.MILLISECONDS);
		
		if (pesoEntity.getUsuario().getRutina() == null)
			throw new MyException("Primero debes tener una rutina asignada.");
		
		if (diferenciaDias > 5 ) {
			UsuarioEntity usuario = pesoEntity.getUsuario();
			double diasGr = 0;
			
			// depende del tipo de rutina deberia de ganar o perder peso / tiempo
			if (usuario.getRutina().getTipo().equals(TipoEj.DEFINICION)) {
				diasGr = -0.040;
			} else if (usuario.getRutina().getTipo().equals(TipoEj.HIPERTROFIA)) {
				diasGr = 0.028;
			} else if (usuario.getRutina().getTipo().equals(TipoEj.FUNCIONAL) || usuario.getRutina().getTipo().equals(TipoEj.MIXTO)) {
				diasGr = 0.014;
			} else if (usuario.getRutina().getTipo().equals(TipoEj.FUERZA)) {
				diasGr = 0.020;
			}
			
			// calcular la variacion real del peso en el tiempo
			double difGrReal = (peso - pesoEntity.getPeso()) / diferenciaDias;
			
			// variacion entre la real y la estimada (invertida)
			double varDif = diasGr - difGrReal;
			
			if (diferenciaDias < 30) {
				// cambiar kcal
				int cambioK = (int)(varDif / 0.004) * 30;
				if (cambioK > 300) {
					cambioK = 300;
				} else if (cambioK < -300) {
					cambioK = -300;
				}
				usuario.setKCal(pesoEntity.getUsuario().getKCal() + cambioK);
				usuario = usuarioRepositorio.save(usuario);
			}
			
			ModelMapper modelMapper = new ModelMapper();
			pesoEntity = pesoRepositorio.save(PesoEntity.builder().peso(peso).usuario(usuario).fecha(new Date()).build());
			
			return modelMapper.map(pesoEntity, PesoDTO.class);
		} else {
			throw new ConflictException("Hace menos de 5 días que se ha guardado el peso por última vez.");
		}
	}
}