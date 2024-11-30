package wisehero.springbootobservability.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	// 4xx
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
	DUPLICATE_ENTITY(HttpStatus.BAD_REQUEST, "이미 존재하는 데이터입니다."),

	// 5xx
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String message;
}
