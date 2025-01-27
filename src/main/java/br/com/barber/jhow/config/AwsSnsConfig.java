package br.com.barber.jhow.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSnsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretAccessKey;

    @Value("${aws.sns.topic.emit.email.arn}")
    private String emitEmailArn;

    @Bean
    public AmazonSNS amazonSNS() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKeyId, this.secretAccessKey);

        return AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    @Bean(name = "emitEmailEventsTopic")
    public Topic snsEmailTopic() {
        return new Topic().withTopicArn(this.emitEmailArn);
    }

}
