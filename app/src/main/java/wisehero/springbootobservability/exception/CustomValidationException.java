package wisehero.springbootobservability.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomValidationException extends RuntimeException {

	private final String detail;

	public CustomValidationException(String message, String detail) {
		super(message);
		this.detail = detail;
	}

}
