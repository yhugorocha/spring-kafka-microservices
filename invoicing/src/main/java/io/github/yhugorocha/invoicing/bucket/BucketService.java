package io.github.yhugorocha.invoicing.bucket;

import io.github.yhugorocha.invoicing.config.props.MinioProps;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class BucketService {

    private final MinioClient bucketClient;
    private final MinioProps minioProps;

    public void upload(BucketFile bucketFile){
        try {
            var object = PutObjectArgs.builder()
                    .bucket(minioProps.getBucketName())
                    .object(bucketFile.name())
                    .stream(bucketFile.is(), bucketFile.size(), -1)
                    .contentType(bucketFile.contentType().toString())
                    .build();

            bucketClient.putObject(object);
        } catch (Exception e) {
            log.error("Failed to upload file to bucket", e);
            throw new RuntimeException("Failed to upload file to bucket", e);
        }
    }

    public String getUrl(String fileName){
        try {
            return bucketClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProps.getBucketName())
                            .object(fileName)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to get file URL from bucket", e);
            throw new RuntimeException("Failed to get file URL from bucket", e);
        }
    }
}
