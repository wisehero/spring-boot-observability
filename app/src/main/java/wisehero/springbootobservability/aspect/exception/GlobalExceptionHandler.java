package wisehero.springbootobservability.aspect.exception;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;
import wisehero.springbootobservability.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public ErrorResponse handleDuplicateKeyException(DuplicateKeyException e) {
		log.warn("DuplicateKeyException 발생", e);
		return ErrorResponse.of(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다.");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
		log.warn("IllegalArgumentException 발생", e);
		return ErrorResponse.of(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다.", e.getMessage());
	}

	@ExceptionHandler(BindException.class) // MethodArgumentNotValidException은 BindException을 상속받음
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다.");
		List<FieldError> fieldErrors = e.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return errorResponse;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.warn("HttpRequestMethodNotSupportedException 발생", e);
		return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method 입니다.");
	}

	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNoResourceFoundException(NoResourceFoundException e) {
		log.warn("NoResourceFoundException 발생 Input URL : {}", e.getResourcePath(), e);
		return ErrorResponse.of(HttpStatus.NOT_FOUND, "요청하신 자원을 찾을 수 없습니다.");
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(Exception e) {
		log.error("처리되지 않은 Exception 발생", e);
		return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
	}
}
