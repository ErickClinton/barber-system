package br.com.barber.jhow.service;

import br.com.barber.jhow.service.dto.MessageDto;
import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.stereotype.Service;

@Service
public class AwsSnsService {

    private final AmazonSNS snsClient;

    public AwsSnsService(AmazonSNS snsClient) {
        this.snsClient = snsClient;
    }

    public void publish(String topic,MessageDto message) {
        this.snsClient.publish(topic,message.message());
    }
}
