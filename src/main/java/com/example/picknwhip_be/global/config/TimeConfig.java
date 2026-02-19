package com.example.picknwhip_be.global.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean(name = "kstClock")
  public Clock kstClock() {
    // 픽업/일정 등 "KST 고정"이 필요한 곳에서만 사용
    return Clock.system(ZoneId.of("Asia/Seoul"));
  }
}
