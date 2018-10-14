package com.sainsbury.scrape;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication(scanBasePackages = { "com.sainsbury.scrape" })
public class ScrapeApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ScrapeApplication.class, args);
	}

	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("scrape-shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
	}

}