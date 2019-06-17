package appAdictive.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import appAdictive.dto.RutinaPageDTO;
import appAdictive.dto.RutinaSearch;
import appAdictive.entity.RutinaEntity;
import appAdictive.util.enums.TipoEj;

public interface RutinaRepositorio extends JpaRepository<RutinaEntity, Integer> {
	
	@Query("SELECT r.nombre FROM Rutina r WHERE r.creador IS NOT NULL AND UPPER(r.nombre) = UPPER(:nombre)")
	public Optional<String> existsPubOfi(String nombre);
	
	public Optional<RutinaEntity> findByNombre(String nombre);
	
	@Query("SELECT r.nombre FROM Rutina r WHERE r.creador IS NOT NULL AND r.usuario IS NULL")
	public List<String> rutinasPublicas();

	@Query("SELECT new appAdictive.dto.RutinaPageDTO ( r.idRutina, r.nombre , r.copias, r.descripcionDias, r.tipo, r.creador) "
			+ "FROM Rutina r WHERE r.usuario.email = :email")
	public Page<RutinaPageDTO> misRutinasPage(@Param("email") String email, Pageable page);
	
	@Query("SELECT new appAdictive.dto.RutinaPageDTO ( r.idRutina, r.nombre , r.copias, r.descripcionDias, r.tipo, r.creador) "
			+ "FROM Rutina r WHERE r.creador IS NOT NULL AND r.creador != 'ADMIN' AND r.usuario.email IS NULL")
	public Page<RutinaPageDTO> rutinasPublicasPage(Pageable page);
	
	@Query("SELECT new appAdictive.dto.RutinaPageDTO ( r.idRutina, r.nombre , r.copias, r.descripcionDias, r.tipo, r.creador) "
			+ "FROM Rutina r WHERE r.creador = 'ADMIN' AND r.usuario.email IS NULL")
	public Page<RutinaPageDTO> rutinasOficialesPage(Pageable page);
	
	@Query("SELECT r FROM Rutina r WHERE r.creador = :email and r.usuario IS NULL")
	public List<RutinaEntity> rutinasFromUsuario(@Param("email") String email);
	
	@Query("SELECT r.idRutina FROM Rutina r WHERE r.usuario IS NULL AND r.creador = 'ADMIN' AND r.tipo = :tipo AND r.numeroDias <= :numeroDias")
	public List<Integer> idRutinasOficialesPers(@Param("tipo") TipoEj tipo, @Param("numeroDias") int numeroDias);
	
	@Query("SELECT new appAdictive.dto.RutinaSearch ( r.idRutina, r.nombre ) "
			+ "FROM Rutina r WHERE "
			+ "( UPPER(r.nombre) like '%'|| UPPER(:nombre) ||'%' )"
			+ "AND (r.usuario = NULL) "
			+ "AND (r.creador != NULL) "
			+ "AND (r.creador != 'ADMIN')")
	public List<RutinaSearch> buscaNombre (@Param("nombre") String nombre);
	
	@Query("SELECT r FROM Rutina r where r.idRutina = :id AND (r.usuario IS NULL OR r.usuario.email = :email)")
	public Optional<RutinaEntity> buscaRutina (@Param("email") String email, @Param("id") Integer id);
	
	@Query("SELECT COUNT(r) FROM Rutina r WHERE fecha_creacion > :fecha AND r.usuario IS NULL AND creador = 'ADMIN'")
	public int estadisticasOficial (@Param("fecha") Date fecha);
	
	@Query("SELECT COUNT(r) FROM Rutina r WHERE fecha_creacion > :fecha AND r.usuario IS NULL AND creador != 'ADMIN'")
	public int estadisticasPublica (@Param("fecha") Date fecha);
	
	@Query("SELECT COUNT(r) FROM Rutina r WHERE fecha_creacion > :fecha AND r.usuario IS NOT NULL AND creador IS NULL")
	public int estadisticasPrivada (@Param("fecha") Date fecha);
}
