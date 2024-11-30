package wisehero.springbootobservability.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends BusinessException {

	private static final String MESSAGE = "잘못된 요청입니다.";
	private String detail;

	public InvalidRequestException() {
		super(MESSAGE);
	}

	public InvalidRequestException(String detail) {
		super(MESSAGE);
		this.detail = detail;
	}
}
