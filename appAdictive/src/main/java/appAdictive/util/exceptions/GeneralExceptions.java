package appAdictive.util.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.spi.ErrorMessage;
import org.postgresql.util.PSQLException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class GeneralExceptions {
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
	    String error = ex.getName() + " tiene que ser del tipo " + ex.getRequiredType().getName();
	
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrittyException(DataIntegrityViolationException e) {
       return new ResponseEntity<>("Ha habido un error de integridad. " , HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintException(ConstraintViolationException e) {
        return new ResponseEntity<>("Error de fk. " , HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> psqlException(PSQLException e) {
        return new ResponseEntity<>("Error tipo de dato. " , HttpStatus.CONFLICT);
    }

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<String> handleConverterErrors(JsonMappingException e) {
		String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        ErrorMessage errorMessage = new ErrorMessage(message);
        	ResponseEntity<String> response;
        if (errorMessage.getMessage().toString().contains("enums")) {
        	response = new ResponseEntity<>("Se ha dado un dato 'enumerado' mal. " + errorMessage , HttpStatus.CONFLICT);
        } else {
        	response = new ResponseEntity<>("Ha ocurrido un error.  " + errorMessage.getMessage() , HttpStatus.CONFLICT);
        }
		return response;
    }
}