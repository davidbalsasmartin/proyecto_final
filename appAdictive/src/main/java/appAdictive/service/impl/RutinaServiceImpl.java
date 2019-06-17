package appAdictive.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import appAdictive.dto.CreaRutinaDTO;
import appAdictive.dto.Estadisticas;
import appAdictive.dto.RutinaDTO;
import appAdictive.dto.RutinaPageDTO;
import appAdictive.dto.RutinaPersDTO;
import appAdictive.dto.RutinaSearch;
import appAdictive.entity.DiaEntity;
import appAdictive.entity.RutinaEntity;
import appAdictive.entity.UsuarioEntity;
import appAdictive.repository.EjercicioRepositorio;
import appAdictive.repository.RutinaRepositorio;
import appAdictive.repository.UsuarioRepositorio;
import appAdictive.service.RutinaService;
import appAdictive.util.enums.InfoTipo;
import appAdictive.util.enums.Role;
import appAdictive.util.enums.TipoEj;
import appAdictive.util.exceptions.BadRequestException;
import appAdictive.util.exceptions.ConflictException;
import appAdictive.util.exceptions.MyException;
import appAdictive.util.exceptions.NotFoundException;

@Service
public class RutinaServiceImpl implements RutinaService {

	@Autowired
	private RutinaRepositorio rutinaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private EjercicioRepositorio ejercicioRepositorio;

	public RutinaServiceImpl(RutinaRepositorio rutinaRepositorio) {
		this.rutinaRepositorio = rutinaRepositorio;
	}
	
	@Override
	public List<RutinaSearch> buscaNombre(String nombre) {
		return rutinaRepositorio.buscaNombre(nombre);
	}
	
	@Override
	public RutinaDTO buscaRutina(String email, Integer id) throws MyException {
		RutinaEntity rutinaEntity = rutinaRepositorio.buscaRutina(email, id).orElseThrow(() -> new NotFoundException("La rutina buscada no existe."));
		
		ModelMapper modelMapper = new ModelMapper();
		RutinaDTO resultado = modelMapper.map(rutinaEntity, RutinaDTO.class);
		if (rutinaEntity.getUsuario() != null) {
			resultado.setIdUsuario(rutinaEntity.getUsuario().getEmail());
		}
		return resultado;
		
	}
	
	@Override
	public boolean updateUser(String email, int diasDisponibles, TipoEj meta) throws MyException {
		UsuarioEntity usuarioEntity = usuarioRepositorio.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuario no encontrado. "));
		// comprueba que pueda interferir en la rutina actual
		if (usuarioEntity.getRutina().getNumeroDias() <= diasDisponibles && usuarioEntity.getRutina().getTipo() == meta) {
			usuarioEntity.setDiasDisponibles(diasDisponibles);
			usuarioRepositorio.save(usuarioEntity);
			return true;
		}
		
		// en caso de tener una rutina oficial
		if (usuarioEntity.getRutina().getCreador() != null && usuarioEntity.getRutina().getCreador().equals(Role.ADMIN.toString())) {
			if (usuarioEntity.getMeta() != meta)
				usuarioEntity.setCiclo(4);
			usuarioEntity.setDiasDisponibles(diasDisponibles);
			usuarioEntity.setMeta(meta);
			usuarioRepositorio.save(asignarRutinaAuto(usuarioEntity));
			return true;
		}
		
		throw new ConflictException("Este cambio interfiere en la rutina privada actual, primero se debe cambiar a un tipo de rutina oficial, o "
				+ "a uno con las mismas carácterísticas que a las que se buscan cambiar.");
	}
	
	@Override
	public RutinaPersDTO home (String email) throws MyException {
		UsuarioEntity usuario = usuarioRepositorio.findById(email).orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
		
		// si la rutina le ha expirado, asignar otra
		if (usuario.getFechaFinal().compareTo(new Date()) < 0)
			usuario = asignarRutinaAuto(usuario);
		
		ModelMapper modelMapper = new ModelMapper();
		RutinaPersDTO rutinaPersDTO = modelMapper.map(usuario.getRutina(), RutinaPersDTO.class);
		rutinaPersDTO.setKcal(usuario.getKCal());
		rutinaPersDTO.setNumeroDias(usuario.getDiasDisponibles());
		rutinaPersDTO.setFechaFinal(usuario.getFechaFinal());

		// calcula la diferencia de semanas
		int semanasDif = (int) ((new Date().getTime()-rutinaPersDTO.getFechaFinal().getTime())/86400000)/7;
		// si la rutina es ABA se debe alternar cada semana, una ABA y otra BAB
		if (rutinaPersDTO.getDescripcionDias() == InfoTipo.ABA && semanasDif % 2 == 0) {
			Collections.reverse(rutinaPersDTO.getDias());
		}
		return rutinaPersDTO;
	}
	
	@Override
	public boolean creaRutina(CreaRutinaDTO rutinaDTO, String email) throws MyException {

		RutinaEntity rutinaEntity = creadorRutina(rutinaDTO);
		
        rutinaEntity.setUsuario(usuarioRepositorio.getOne(email));
        
        rutinaEntity.setFechaCreacion(new Date());
		
		rutinaRepositorio.save(rutinaEntity);
			
		return true;
	}
	
	// crea la rutina sin poner el usuario, y poniendo el creador como admin para hacer oficial
	@Override
	public boolean creaRutinaAdmin(CreaRutinaDTO rutinaDTO) throws MyException {
		
		if (rutinaRepositorio.existsPubOfi(rutinaDTO.getNombre()).isPresent()) throw new MyException("Ya hay una rutina publicada con ese nombre. ");
		
		RutinaEntity rutinaEntity = creadorRutina(rutinaDTO);
		
        rutinaEntity.setCreador(Role.ADMIN.toString());
        
        rutinaEntity.setFechaCreacion(new Date());
		
		rutinaRepositorio.save(rutinaEntity);
			
		return true;
	}
	
	private RutinaEntity creadorRutina (CreaRutinaDTO rutinaDTO) throws MyException {
		if (rutinaDTO.getDias().size() != rutinaDTO.getDescripcionDias().getValue())
			throw new BadRequestException("Esta rutina no contiene el número de días esperados. ");
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		RutinaEntity rutinaEntity = modelMapper.map(rutinaDTO, RutinaEntity.class);
		rutinaEntity.setNumeroDias(rutinaDTO.getDescripcionDias().toString().length());
		
		List<Integer> ejercicioIds = ejercicioRepositorio.findIds();
		
		Set<Integer> sinRepetidos = new HashSet<>();
		
		for (DiaEntity dia : rutinaEntity.getDias()) {
			dia.setRutina(rutinaEntity);
			
			if (dia.getDiaEjercicios().size() > 8 || dia.getDiaEjercicios().size() < 3)
				throw new ConflictException("Debe haber entre 3 y 8 ejercicios al día. ");
			
				dia.getDiaEjercicios().forEach(diaEjercicio -> {
						diaEjercicio.setDia(dia);
						sinRepetidos.add(diaEjercicio.getEjercicio().getIdEjercicio());
						diaEjercicio.setEjercicio(ejercicioRepositorio.getOne(diaEjercicio.getEjercicio().getIdEjercicio()));
				});
		}

		// Mira que ejercicios no existen
        List<Integer> result = sinRepetidos.stream().filter(noRepetido -> !ejercicioIds.contains(noRepetido)).collect(Collectors.toList());
        
        if (!result.isEmpty()) throw new NotFoundException("Hay ejercicios que no existen. ");
        
        return rutinaEntity;
	}
	
	@Override
	public Estadisticas obtieneEstadistica () {
		// calcula la fecha justo hace una semana
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		int oficiales = rutinaRepositorio.estadisticasOficial(calendar.getTime());
		int publicas = rutinaRepositorio.estadisticasPublica(calendar.getTime());
		int privadas = rutinaRepositorio.estadisticasPrivada(calendar.getTime());
		int baneo = usuarioRepositorio.estadisticasBan();
		int noBaneo = usuarioRepositorio.estadisticasNoBan();

		return new Estadisticas(oficiales, publicas, privadas, baneo, noBaneo);
	}
	
	@Override
	public boolean publicar (Integer id, String email) throws MyException {
		RutinaEntity rutina = rutinaRepositorio.findById(id).orElseThrow(() -> 
			new NotFoundException("Esta rutina no existe. "));
		
		if (rutina.getUsuario() == null || (rutina.getUsuario() != null && !rutina.getUsuario().getEmail().equals(email)))
			throw new ConflictException("Esa rutina no pertenece a este usuario. ");
		
		if (rutina.getCreador() != null)
			throw new MyException("Esta rutina ya ha sido publicada. ");
		
		if (rutinaRepositorio.rutinasPublicas().contains(rutina.getNombre()))
			throw new MyException("Ya hay una rutina publicada con ese nombre. ");
		
		// modifica la del usuario para indicar que ha sido publicada, este puede seguir usandola de forma privada, pero no publicarla mas
		rutina.setCreador(email);
		rutinaRepositorio.save(rutina);
		
		// Publica un duplicado para no perder esta en nuestro listado
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		CreaRutinaDTO rutinaDTO = modelMapper.map(rutina, CreaRutinaDTO.class);
		rutina = creadorRutina(rutinaDTO);
		
		rutina.setCreador(email);
		rutina.setFechaCreacion(new Date());
		rutina.setUsuario(null);
		rutinaRepositorio.save(rutina);
		
		return true;
	}
	
	@Override
	public boolean oficial(Integer id) throws MyException {
		RutinaEntity rutinaEntity = rutinaRepositorio.findById(id).orElseThrow(() -> 
			new NotFoundException("Esta rutina no existe. "));
		
		if (rutinaEntity.getCreador().contains(Role.ADMIN.toString())) 
			throw new MyException("Esta rutina ya es oficial. ");
		
		if (rutinaEntity.getCreador() == null || rutinaEntity.getUsuario() != null)
			throw new ConflictException("Esta rutina es privada. ");
		
		rutinaEntity.setCopias(0);
		rutinaEntity.setCreador(Role.ADMIN.toString());
		rutinaEntity.setFechaCreacion(new Date());
		rutinaRepositorio.save(rutinaEntity);
			
		return true;
	}
	
	@Override
	public boolean copiar(Integer id, String email) throws MyException {
		RutinaEntity rutina = rutinaRepositorio.findById(id).orElseThrow(() -> 
			new NotFoundException("Esta id no corresponde a ninguna rutina. "));
		
		if (rutina.getCreador() == null)
			throw new ConflictException("Esta rutina no es pública. ");
		
		String creador = rutina.getCreador();

		// Anade +1 copia a la original
		if (!creador.contentEquals(email)) {
			rutina.setCopias(rutina.getCopias() + 1);
			rutinaRepositorio.save(rutina);
		}
		
		// Crea la copia
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		CreaRutinaDTO rutinaDTO = modelMapper.map(rutina, CreaRutinaDTO.class);
		rutina = creadorRutina(rutinaDTO);
		
		// Guarda la copia como tal
		rutina.setIdRutina(null);
		rutina.setCopias(0);
		rutina.setFechaCreacion(new Date());
		rutina.setCreador(creador);
		rutina.setUsuario(usuarioRepositorio.getOne(email));
		rutinaRepositorio.save(rutina);
		
		return true;
	}
	
	@Override
	public boolean elimina (Integer id, String email) throws MyException {
		RutinaEntity rutinaEntity = rutinaRepositorio.findById(id).orElseThrow(() -> new NotFoundException("Esta rutina no existe"));
		UsuarioEntity usuarioReal = usuarioRepositorio.findByEmail(email).get();
		
		if (usuarioReal.getRole() == Role.USER && rutinaEntity.getUsuario() != null && rutinaEntity.getUsuario().getEmail().equals(usuarioReal.getEmail())) {
			if (rutinaEntity.getUsuario().getRutinaAnterior() == rutinaEntity) {
				usuarioReal.setRutinaAnterior(null);
				usuarioRepositorio.save(usuarioReal);
			}
			if (rutinaEntity.getUsuario().getRutina() == rutinaEntity)
				throw new MyException("Primero debes cambiar de rutina actual.");
			
			rutinaRepositorio.delete(rutinaEntity);
			return true;
		} else if (usuarioReal.getRole() == Role.ADMIN && rutinaEntity.getUsuario() == null && rutinaEntity.getCreador() != null && rutinaEntity.getCreador() != Role.ADMIN.toString()) {
			rutinaRepositorio.delete(rutinaEntity);
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean asignarRutinaManual (Integer idRutina, String email) throws MyException {
		RutinaEntity rutina = rutinaRepositorio.findById(idRutina)
				.orElseThrow(() -> new NotFoundException("La rutina buscada no existe. "));
		
		if (rutina.getUsuario() == null) {
			throw new ConflictException("Esta rutina es pública. ");
		} else if (!rutina.getUsuario().getEmail().equals(email)) {
			throw new ConflictException("Esta rutina es de otro usuario. ");
		}
		
		UsuarioEntity usuario = rutina.getUsuario();
		
		if (usuario.getDiasDisponibles() < rutina.getNumeroDias())
			usuario.setDiasDisponibles(rutina.getNumeroDias());
			
		rutina.getUsuario().setRutinaAnterior(rutina.getUsuario().getRutina());
		
		// Que termine su rutina en 100 anios
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 100);
		
		rutina.getUsuario().setRutina(rutina);
		usuario.setMeta(rutina.getTipo());
		rutina.getUsuario().setFechaFinal(c.getTime());
		rutina.getUsuario().setCiclo(0);
		
		usuarioRepositorio.save(rutina.getUsuario());
		
		return true; 
	}
	
	@Override
	public boolean asignaRutinaAutoFromServer (String email) throws MyException {
		asignarRutinaAuto(usuarioRepositorio.getOne(email));
		return true;
	}
	
	private UsuarioEntity asignarRutinaAuto (UsuarioEntity usuario) throws MyException {
		if (usuario.getCiclo() != 3) {
			
			usuario.setCiclo(usuario.getCiclo() > 3 ? 1 : usuario.getCiclo() +1);

			TipoEj meta = usuario.getMeta();
			
			if (usuario.getMeta() == TipoEj.HIPERTROFIA && usuario.getCiclo() == 3)
				meta = ((int) (Math.random() * 2)) == 1 ? TipoEj.FUERZA : TipoEj.MIXTO;
			
			// Comprobamos las rutinas oficiales que pueden ser válidas
			List<Integer> idsOficiales = rutinaRepositorio.idRutinasOficialesPers(meta, usuario.getDiasDisponibles());
			
			// Descartamos la rutina actual y la anterior
			if (usuario.getRutina() != null && idsOficiales.contains(usuario.getRutina().getIdRutina()))
				idsOficiales.remove(usuario.getRutina().getIdRutina());
			
			if (usuario.getRutinaAnterior() != null && idsOficiales.contains(usuario.getRutinaAnterior().getIdRutina()))
				idsOficiales.remove(usuario.getRutinaAnterior().getIdRutina());
			
			if (idsOficiales.size() == 0) {
				throw new NotFoundException("No hay ninguna rutina que cumpla los requisitos actualmente, por favor, asígnate una manualmente. ");
			}
			
			// Averiguamos la nueva rutina de forma aleatoria
			int idPosicion = (int) (Math.random() * (idsOficiales.size() - 1));
			
			usuario.setRutinaAnterior(usuario.getRutina());
			
			usuario.setRutina(rutinaRepositorio.getOne(idsOficiales.get(idPosicion)));

		} else {
			usuario.setCiclo(4);
			usuario.setRutina(rutinaRepositorio.getOne(new Integer(1)));
		}
		
		// Seteamos la fecha de fin segun su duracion
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    	c.add(Calendar.WEEK_OF_YEAR, usuario.getRutina().getTipo().getDuracion());
    	usuario.setFechaFinal(c.getTime());
    	
		return usuarioRepositorio.save(usuario);
	}
	
	@Override
	public Page<RutinaPageDTO> misRutinasPage(String email, Pageable page) {
		return 	rutinaRepositorio.misRutinasPage(email, page);
	}
	
	@Override
	public Page<RutinaPageDTO> rutinasPublicasPage(Pageable page, boolean oficial) {
		return oficial ? rutinaRepositorio.rutinasOficialesPage(page) : rutinaRepositorio.rutinasPublicasPage(page);
	}
	
	@Override
	public boolean modificaRutina(RutinaDTO rutinaDTO, String email) throws MyException {
		RutinaEntity rutinaEntity = rutinaRepositorio.findById(rutinaDTO.getIdRutina()).orElseThrow(
				() -> new NotFoundException("La rutina '" + rutinaDTO.getIdRutina() + "' no existe. "));
		
		if (rutinaEntity.getCreador() != null)
			new ConflictException("La rutina '" + rutinaDTO.getIdRutina() + "' no es privada. ");
		
		if (rutinaEntity.getUsuario() != null && !rutinaEntity.getUsuario().getEmail().equals(email))
			throw new ConflictException("La rutina '" + rutinaDTO.getIdRutina() + "' no es del usuario " + email + ". ");
		
		rutinaEntity.setNombre(rutinaDTO.getNombre());
		
		rutinaEntity.setDescripcion(rutinaDTO.getDescripcion());
		
		rutinaRepositorio.save(rutinaEntity);
			
		return true;
	}
}