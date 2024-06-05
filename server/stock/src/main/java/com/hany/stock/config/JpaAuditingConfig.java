package com.hany.stock.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 자동으로 날짜를 설정하도록 jpa에 지시
public class JpaAuditingConfig {
}
