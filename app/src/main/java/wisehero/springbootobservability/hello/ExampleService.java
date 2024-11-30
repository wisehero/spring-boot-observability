package wisehero.springbootobservability.hello;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import wisehero.springbootobservability.hello.request.ExampleModelAttribute;
import wisehero.springbootobservability.hello.request.ExampleRequestBody;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

	private final ExampleRepository exampleRepository;

	public void requestParamInService(String name, String description) {
		log.debug("name: {}, description: {}", name, description);
		log.info("name: {}, description: {}", name, description);
	}

	public void modelAttributeInService(ExampleModelAttribute exampleModelAttribute) {
		log.debug("exampleModelAttribute: {}", exampleModelAttribute);
		log.info("exampleModelAttribute: {}", exampleModelAttribute);
	}

	public void pathVariableInService(Long id) {
		log.debug("id: {}", id);
		log.info("id: {}", id);
	}

	public void pathVariableWithRequestParamInService(Long id, String name) {
		log.debug("id: {}, name: {}", id, name);
		log.info("id: {}, name: {}", id, name);
	}

	public void pathVariableWithModelAttributeInService(Long id, ExampleModelAttribute exampleModelAttribute) {
		log.debug("id: {}, exampleModelAttribute: {}", id, exampleModelAttribute);
		log.info("id: {}, exampleModelAttribute: {}", id, exampleModelAttribute);
	}

	@Transactional
	public void requestBodyInService(ExampleRequestBody exampleRequestBody) {
		log.debug("exampleRequestBody: {}", exampleRequestBody);
		log.info("exampleRequestBody: {}", exampleRequestBody);

		ExampleEntity exampleEntity = ExampleEntity.of(exampleRequestBody);
		exampleRepository.save(exampleEntity);
	}

	@Transactional
	public void requestBodyWithPathVariableInService(Long id, ExampleRequestBody exampleRequestBody) {
		log.debug("id: {}, exampleRequestBody: {}", id, exampleRequestBody);
		log.info("id: {}, exampleRequestBody: {}", id, exampleRequestBody);
		ExampleEntity entity = exampleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Not found"));

		entity.updateEntity(exampleRequestBody);
		exampleRepository.save(entity);
	}
}
