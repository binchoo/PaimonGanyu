package org.binchoo.paimonganyu.awsutils.s3;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.binchoo.paimonganyu.awsutils.JsonPayloadAwsEventWrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Reads all s3 objects that S3Event refers, and convert their contents to java POJOs.
 * It assumes that those s3 objects have json formatted contents.
 * @author : jbinchoo
 * @since : 2022/04/17
 */
@Slf4j
public class S3EventObjectReader extends JsonPayloadAwsEventWrapper<S3Event> {

    private final AmazonS3 s3Client;

    public S3EventObjectReader(AmazonS3 s3Client) {
        this(s3Client, new ObjectMapper());
    }

    public S3EventObjectReader(AmazonS3 s3Client, ObjectMapper objectMapper) {
        super(objectMapper);
        this.s3Client = s3Client;
    }

    @Override
    protected Stream<String> getJsonStream(S3Event event) {
        return event.getRecords().stream()
                .map(s3Record-> {
                    String bucketName = s3Record.getS3().getBucket().getName();
                    String objectKey = s3Record.getS3().getObject().getKey();
                    String json = readS3Object(bucketName, objectKey);
                    log.debug("S3ObjectJson: {}", json);
                    return json;
                })
                .filter(Objects::nonNull);
    }

    private String readS3Object(String bucketName, String objectKey) {
        S3ObjectInputStream inputStream = s3Client.getObject(bucketName, objectKey).getObjectContent();
        try (inputStream) {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            log.error("Could not read s3 object {}/{}", bucketName, objectKey);
            log.error("With exception", e);
        }
        return null;
    }
}
