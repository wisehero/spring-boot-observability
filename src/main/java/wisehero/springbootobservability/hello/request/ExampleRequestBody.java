package wisehero.springbootobservability.hello.request;

public record ExampleRequestBody(
	String name,
	Integer age,
	String description
) {
}
