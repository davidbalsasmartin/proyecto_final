package appAdictive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import appAdictive.entity.Token;
 
public interface TokenRepositorio extends JpaRepository<Token, Integer> {
	Optional<Token> findByToken(String Token);
}
