package appAdictive.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import appAdictive.entity.EjercicioEntity;
 
public interface EjercicioRepositorio extends JpaRepository<EjercicioEntity, Integer> {
	
	@Query("SELECT e FROM Ejercicio e WHERE UPPER(e.nombre) = UPPER(:nombre)")
	Optional<EjercicioEntity> existe(String nombre);
	
	@Query("SELECT idEjercicio FROM Ejercicio e")
	List<Integer> findIds();
	
	List<EjercicioEntity> findAllByOrderByNombre();
}
