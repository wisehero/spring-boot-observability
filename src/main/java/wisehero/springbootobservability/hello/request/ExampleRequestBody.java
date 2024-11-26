package wisehero.springbootobservability.hello.request;

public record ExampleRequestBody(
	String name,
	String age,
	String description
) {
}
