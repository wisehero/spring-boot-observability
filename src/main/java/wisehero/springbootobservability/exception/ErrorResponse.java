package wisehero.springbootobservability.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class ErrorResponse {

	private int code;
	private HttpStatus status;
	private String message;
	private String detail;
	private Map<String, String> validationErrors = new HashMap<>();

	@Builder
	private ErrorResponse(int code, HttpStatus status, String message, String detail) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.detail = detail;
	}

	public static ErrorResponse of(HttpStatus httpStatus, String message) {
		return new ErrorResponse(httpStatus.value(), httpStatus, message, null);
	}

	public static ErrorResponse of(HttpStatus httpStatus, String message, String detail) {
		return new ErrorResponse(httpStatus.value(), httpStatus, message, detail);
	}

	public void addValidation(String fieldName, String errorMessage) {
		validationErrors.put(fieldName, errorMessage);
	}
}
