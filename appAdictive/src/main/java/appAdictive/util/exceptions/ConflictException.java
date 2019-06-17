package appAdictive.util.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends MyException {

	private static final long serialVersionUID = 1L;

	public ConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConflictException(String message) {
		super(message);
	}

	public ConflictException(Throwable cause) {
		super(cause);
	}

}
