package appAdictive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import appAdictive.entity.PesoEntity;
import appAdictive.entity.UsuarioEntity;
 
public interface PesoRepositorio extends JpaRepository<PesoEntity, Integer> {
	
	List<PesoEntity> findFirst8ByUsuarioOrderByFechaDesc(UsuarioEntity usuarioEntity);
	
	PesoEntity findFirstByUsuarioOrderByFechaDesc(UsuarioEntity usuarioEntity);

}
