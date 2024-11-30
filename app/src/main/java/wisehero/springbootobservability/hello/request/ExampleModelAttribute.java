package wisehero.springbootobservability.hello.request;

import jakarta.validation.constraints.NotBlank;

public record ExampleModelAttribute(
	@NotBlank(message = "이름은 필수입니다.")
	String name,
	String description
) {
}
