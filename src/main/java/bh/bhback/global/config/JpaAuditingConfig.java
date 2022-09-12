package bh.bhback.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@WebMvcTest 사용 시, JpaAuditing 에러를 방지 하기 위해 따로 분리
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {
}
