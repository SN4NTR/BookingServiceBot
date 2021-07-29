package com.amiron.booking.bot.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aliaksandr Miron
 */
@ConfigurationProperties("bot")
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Data
public class BotConfigProperties {

    private String name;

    private String token;
}
