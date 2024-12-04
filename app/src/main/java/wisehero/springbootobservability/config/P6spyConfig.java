package wisehero.springbootobservability.config;

import org.springframework.context.annotation.Configuration;

import com.p6spy.engine.spy.P6SpyOptions;

import jakarta.annotation.PostConstruct;
import wisehero.springbootobservability.util.P6spyPrettySqlFormatter;

@Configuration
public class P6spyConfig {
	@PostConstruct
	public void setLogMessageFormat() {
		P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
	}
}
