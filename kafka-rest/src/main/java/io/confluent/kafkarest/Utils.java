package io.confluent.kafkarest;

import io.confluent.rest.exceptions.RestServerErrorException;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.RetriableException;

import static io.confluent.kafkarest.Errors.KAFKA_ERROR_ERROR_CODE;
import static io.confluent.kafkarest.Errors.KAFKA_RETRIABLE_ERROR_ERROR_CODE;

public class Utils {
    public static final String UNEXPECTED_PRODUCER_EXCEPTION
            = "Unexpected non-Kafka exception returned by Kafka";

    public static int codeFromProducerException(Throwable e) {
        if (e instanceof RetriableException) {
            return KAFKA_RETRIABLE_ERROR_ERROR_CODE;
        } else if (e instanceof KafkaException) {
            return KAFKA_ERROR_ERROR_CODE;
        } else {
            // We shouldn't see any non-Kafka exceptions, but this covers us in case we do see an
            // unexpected error. In that case we fail the entire request -- this loses information
            // since some messages may have been produced correctly, but is the right thing to do from
            // a REST perspective since there was an internal error with the service while processing
            // the request.
            throw new RestServerErrorException(UNEXPECTED_PRODUCER_EXCEPTION,
                    RestServerErrorException.DEFAULT_ERROR_CODE, e
            );
        }
    }
}
