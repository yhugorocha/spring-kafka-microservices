package io.github.yhugorocha.invoicing.config;

import io.github.yhugorocha.invoicing.config.props.MinioProps;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BucketConfig {

    private final MinioProps minioProps;

    @Bean
    public MinioClient bucketClient() {
        return MinioClient.builder()
                .endpoint(minioProps.getUrl())
                .credentials(minioProps.getAccessKey(), minioProps.getSecretKey())
                .build();
    }
}
