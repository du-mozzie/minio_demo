package com.du.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/18 14:27]
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ffmpeg")
@EnableConfigurationProperties
public class FFmpegProperties {
}
