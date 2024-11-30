package wisehero.springbootobservability.filter;

import static java.nio.charset.StandardCharsets.*;

import java.io.IOException;
import java.net.URLDecoder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HTTPLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws
		IOException,
		ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		long startTime = System.currentTimeMillis();

		String clientIP = getClientIP(httpRequest);
		String requestURI = httpRequest.getRequestURI();
		String queryString = httpRequest.getQueryString();
		String fullPath = queryString != null ? requestURI + "?" + URLDecoder.decode(queryString, UTF_8) : requestURI;

		// IP, URI, QueryString 로깅
		log.info("HTTP CALL START [CLIENT IP:{}] REQUEST URL={}", clientIP, requestURI);
		log.info("Full Path = {}", fullPath);

		// 요청 처리
		try {
			filterChain.doFilter(request, response);
		} finally {
			int status = httpResponse.getStatus();
			long duration = System.currentTimeMillis() - startTime;

			if (status >= 400 && status < 500) {
				log.warn("HTTP CALL FAILED: {} {} - Status: {} - Duration: {} ms",
					httpRequest.getMethod(),
					requestURI,
					status,
					duration);
			} else {
				log.info("HTTP CALL COMPLETED: {} {} - Status: {} - Duration: {} ms",
					httpRequest.getMethod(),
					requestURI,
					status,
					duration);
			}
		}
	}

	private String getClientIP(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");

		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}
}
