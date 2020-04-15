package esperimenti.templateservice.msgAdapters;

import esperimenti.templateservice.operationsParsers.OperationsStringParser;
import esperimenti.templateservice.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListenerAdapter {

    @Autowired
    private OperationsStringParser operationsStringParser;

    @KafkaListener(topics = "${template.kafka.channel.in}", groupId="${template.kafka.groupid}")
    public void listen(ConsumerRecord<String, String> record) {

        String receivedMessage = record.value();
        //log.info("messaggio ricevuto: " + receivedMessage);
        try {
            operationsStringParser.parseOperations(receivedMessage);
        } catch (Exception e ) {
            e.printStackTrace(); //TODO: gestire eccezione (cosa fare?)
        }

    }
}
