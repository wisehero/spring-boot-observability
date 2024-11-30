package wisehero.springbootobservability.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import wisehero.springbootobservability.filter.HTTPLoggingFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<HTTPLoggingFilter> APIFilter() {
		FilterRegistrationBean<HTTPLoggingFilter> registrationBean = new FilterRegistrationBean<>();
		HTTPLoggingFilter apiLoggingFilter = new HTTPLoggingFilter();
		registrationBean.setFilter(apiLoggingFilter);
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}
}
