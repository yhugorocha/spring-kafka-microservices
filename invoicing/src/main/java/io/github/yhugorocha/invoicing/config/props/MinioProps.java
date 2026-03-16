package io.github.yhugorocha.invoicing.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
public class MinioProps {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
