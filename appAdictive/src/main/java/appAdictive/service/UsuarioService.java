package appAdictive.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import appAdictive.dto.UsuarioRegistroDTO;
import appAdictive.entity.UsuarioEntity;
import appAdictive.util.exceptions.MyException;

public interface UsuarioService {
    
    UsuarioEntity buscaEmail(String email);
    
    boolean registro (UsuarioRegistroDTO user)  throws MyException;
    
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    
    boolean cambioContrasena(String userEmail, String nuevaContrasena) throws MyException;
    
    public boolean ban(String email, boolean borraRutinas) throws MyException;
    
    public boolean solicitaOlvidaContrasena(String email)  throws MyException;
}
