package com.pomostudy.config.auditing;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {
    // This class can be empty.
    // Its sole purpose is to hold the @EnableJpaAuditing annotation, allowing it to be
    // enabled for the main application and easily excluded in tests.
}