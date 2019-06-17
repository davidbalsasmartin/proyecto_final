package appAdictive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import appAdictive.entity.UsuarioEntity;
import appAdictive.proyection.ValidateLogin;
 
public interface UsuarioRepositorio extends JpaRepository<UsuarioEntity, String> {
	Optional<UsuarioEntity> findByNombre(String name);
	
	// antes para login ahora no se
	Optional<UsuarioEntity> findByEmail(String name);
	
	Optional<ValidateLogin> findUserByEmail(String name);
	
    @Modifying
    @Query(value = "UPDATE usuario u SET contrasena =:contrasena WHERE u.email = :email", nativeQuery = true)
	void updatePass (@Param("contrasena") String contrasena, @Param("email") String email);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ban = true")
	public int estadisticasBan ();
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ban = false")
	public int estadisticasNoBan ();
}
