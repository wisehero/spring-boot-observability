package wisehero.springbootobservability.aspect.logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerLoggingAspect {

	private final ObjectMapper objectMapper;

	@Around("@within(org.springframework.web.bind.annotation.RestController)")
	public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		// 컨트롤러 클래스명과 메서드명 추출
		String controllerName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();

		// 시작 로그
		log.info("API CALL {}-{} START", controllerName, methodName);

		// 파라미터 로깅
		logParameters(joinPoint);

		// 메서드 실행
		Object result = joinPoint.proceed();

		// 종료 로그
		long executionTime = System.currentTimeMillis() - startTime;
		log.info("API CALL COMPLETED: {}-{} - Execution Time: {}ms",
			controllerName,
			methodName,
			executionTime);

		return result;
	}

	private void logParameters(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		Parameter[] parameters = method.getParameters();
		Object[] args = joinPoint.getArgs();

		// 쿼리 파라미터 로깅
		List<String> queryParams = new ArrayList<>();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(RequestParam.class) ||
				parameter.isAnnotationPresent(ModelAttribute.class)) {
				if (isPrimitiveOrString(parameter)) {
					String paramName = getParameterNameV2(parameter);
					String paramValue = args[i] != null ? args[i].toString() : "null";
					queryParams.add(paramName + "=" + paramValue);
				} else {
					queryParams.addAll(handleQueryParams(args[i]));
				}
			}
		}

		if (!queryParams.isEmpty()) {
			log.info("QueryParams = [{}]", String.join(", ", queryParams));
		}

		// Request Body 로깅
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(RequestBody.class) ||
				parameter.isAnnotationPresent(RequestPart.class)) {
				try {
					String jsonBody = objectMapper.writeValueAsString(args[i]);
					log.info("Request Body = {}", jsonBody);
				} catch (JsonProcessingException e) {
					log.warn("Failed to serialize request body", e);
				}
			}
		}
	}

	private String getParameterName(Parameter parameter) {
		if (parameter.isAnnotationPresent(RequestParam.class)) {
			RequestParam annotation = parameter.getAnnotation(RequestParam.class);
			String value = annotation.value();
			if (!value.isEmpty()) {
				return value;
			}
			String name = annotation.name();
			if (!name.isEmpty()) {
				return name;
			}
		}

		if (parameter.isAnnotationPresent(ModelAttribute.class)) {
			ModelAttribute annotation = parameter.getAnnotation(ModelAttribute.class);
			String value = annotation.value();
			if (!value.isEmpty()) {
				return value;
			}
		}

		return parameter.getName();
	}

	private boolean isPrimitiveOrString(Parameter parameter) {
		Class<?> type = parameter.getType();
		return type.isPrimitive() ||
			type.equals(String.class) ||
			Number.class.isAssignableFrom(type) ||
			type.equals(Boolean.class) ||
			type.equals(Character.class);
	}

	private List<String> handleQueryParams(Object obj) {
		List<String> params = new ArrayList<>();
		if (obj == null) {
			return params;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = field.get(obj);
				String paramName = field.getName();
				String paramValue = value != null ? value.toString() : "null";
				params.add(paramName + "=" + paramValue);
			} catch (IllegalAccessException e) {
				// 예외 처리
				log.error("필드 접근 실패: {}", field.getName(), e);
			}
		}
		return params;
	}

	private String getParameterNameV2(Parameter parameter) {
		RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
		if (requestParam != null && !requestParam.name().isEmpty()) {
			return requestParam.name();
		}
		ModelAttribute modelAttribute = parameter.getAnnotation(ModelAttribute.class);
		if (modelAttribute != null && !modelAttribute.name().isEmpty()) {
			return modelAttribute.name();
		}
		return parameter.getName(); // 파라미터 이름을 기본으로 사용
	}
}
