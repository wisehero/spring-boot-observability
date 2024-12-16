package wisehero.springbootobservability.stressTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class StressController {

	private List<String> list = new ArrayList<>();

	@Autowired
	private DataSource dataSource;

	// CPU 사용량 증가 테스트
	@GetMapping("/cpu")
	public String cpu() {
		log.info("cpu");
		long value = 0;
		for (long i = 0; i < 100000000000L; i++) {
			value++;
		}

		return "ok value=" + value;
	}

	// JVM 메모리 사용량 증가 테스트
	@GetMapping("/jvm-memory")
	public String jvm() {
		log.info("jvm memory");
		for (int i = 0; i < 1000000; i++) {
			list.add("hello jvm!" + i);
		}

		return "ok";
	}

	// DB 커넥션 풀 사용량 증가 테스트
	@GetMapping("/jdbc")
	public String jdbc() throws SQLException {
		log.info("jdbc");
		dataSource.getConnection();
		return "ok";
	}
}
