package io.github.yhugorocha.invoicing.bucket;

import org.springframework.http.MediaType;

import java.io.InputStream;

public record BucketFile(String name, InputStream is, MediaType contentType, long size) {
}
