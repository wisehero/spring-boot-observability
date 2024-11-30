package wisehero.springbootobservability.hello.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExampleRequestBody(
	@NotBlank(message = "이름은 필수입니다.")
	String name,
	@NotNull(message = "나이는 필수입니다.")
	Integer age,
	String description
) {
}
