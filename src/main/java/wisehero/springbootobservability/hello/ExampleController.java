package wisehero.springbootobservability.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wisehero.springbootobservability.hello.request.ExampleModelAttribute;
import wisehero.springbootobservability.hello.request.ExampleRequestBody;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
@Slf4j
public class ExampleController {

	private final ExampleService exampleService;

	@GetMapping
	public void example() {
		log.info("==================EXAMPLE==================");
	}

	@GetMapping("/request-param")
	public void requestParam(@RequestParam("name") String name, @RequestParam("description") String description) {
		exampleService.requestParamInService(name, description);
	}

	@GetMapping("/model-attribute")
	public void modelAttribute(@ModelAttribute ExampleModelAttribute exampleModelAttribute) {
		exampleService.modelAttributeInService(exampleModelAttribute);

	}

	@GetMapping("/path-variable/{id}")
	public void pathVariable(@PathVariable("id") Long id) {
		exampleService.pathVariableInService(id);

	}

	@GetMapping("/path-variable/{id}/with-request-param")
	public void pathVariableWithRequestParam(@PathVariable Long id, @RequestParam String name) {
		exampleService.pathVariableWithRequestParamInService(id, name);
	}

	@GetMapping("/path-variable/{id}/with-model-attribute")
	public void pathVariableWithModelAttribute(@PathVariable Long id,
		@ModelAttribute ExampleModelAttribute exampleModelAttribute) {
		exampleService.pathVariableWithModelAttributeInService(id, exampleModelAttribute);
	}

	@PostMapping("/request-body")
	public void requestBody(@RequestBody ExampleRequestBody exampleRequestBody) {
		exampleService.requestBodyInService(exampleRequestBody);
	}

	@PutMapping("/request-body/with-path-variable/{id}")
	public void requestBodyWithPathVariable(@PathVariable Long id,
		@RequestBody ExampleRequestBody exampleRequestBody) {
		exampleService.requestBodyWithPathVariableInService(id, exampleRequestBody);
	}
}
