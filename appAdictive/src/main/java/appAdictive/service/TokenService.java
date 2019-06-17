package appAdictive.service;

import appAdictive.util.exceptions.MyException;

public interface TokenService {
	public boolean confirmacion (Integer id, String token) throws MyException;
}
