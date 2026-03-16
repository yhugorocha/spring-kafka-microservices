package io.github.yhugorocha.invoicing.api;

import io.github.yhugorocha.invoicing.bucket.BucketFile;
import io.github.yhugorocha.invoicing.bucket.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketController {

    private final BucketService bucketService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        try (InputStream is = file.getInputStream()){
            var mediaType = MediaType.parseMediaType(Objects.requireNonNull(file.getContentType()));
            var bucketFile = new BucketFile(file.getOriginalFilename(), is, mediaType, file.getSize());
            bucketService.upload(bucketFile);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getUrl(@RequestParam("filename") String filename) {
        try {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(bucketService.getUrl(filename)))
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to get file: " + e.getMessage());
        }
    }

}
