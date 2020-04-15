package esperimenti.templateservice.operationsParsers.operationConsumers;

import esperimenti.templateservice.domain.MalformedStringOfOperationsException;
import esperimenti.templateservice.operationsParsers.OperationsStringParser;
import esperimenti.templateservice.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.StringTokenizer;

@Slf4j
@Component
public class ConcurrentConsumer implements OperationConsumer {

    @Autowired
    TemplateService templateService;

    @Override
    public void consume(StringTokenizer st) throws MalformedStringOfOperationsException, InterruptedException {

        String concurrentOperations = OperationsStringParser.checkBracketsAndGetWhatsInside(st, "[", "]");
        //log.info("operazioni concorrenti: " + concurrentOperations);

        StringTokenizer stConcurrentOperations = new StringTokenizer(concurrentOperations, " ");

        // lista contenente le operazioni da eseguire in maniera concorrente
        ArrayList<ArrayList<String>> listOfConcurrentOperations = new ArrayList<>();

        // identifica il tipo di operazione da eseguire
        String operationType;

        // contiene operationType e i suoi argomenti
        ArrayList<String> operation;

        // al momento non esistono operazioni con meno di 3 token (compreso il terminatore ';')
        while(stConcurrentOperations.countTokens() >= 3){

            operation = new ArrayList<>();

            operationType = stConcurrentOperations.nextToken();
            //log.info("operationType: " + operationType);
            operation.add(operationType);

            switch (operationType) {
                case "call":
                case "notify":
                    // servizio target
                    operation.add(stConcurrentOperations.nextToken());

                    // payload
                    operation.add(OperationsStringParser.checkBracketsAndGetWhatsInside(stConcurrentOperations, "{", "}"));
                    break;
                case "exception":
                    // payload
                    operation.add(OperationsStringParser.checkBracketsAndGetWhatsInside(stConcurrentOperations, "\"", "\""));
                    break;
                case "sleep":
                    // sleep time
                    operation.add(stConcurrentOperations.nextToken());

                    // non chiamando checkBracketsAndGetWhatsInside(...) devo controllare e togliere qui il terminatore
                    if(!stConcurrentOperations.nextToken().equals(";"))
                        throw new MalformedStringOfOperationsException("ogni comando deve essere terminato con il carattere ';' anche sleep");
                    break;
                default:
                    throw new MalformedStringOfOperationsException("token inaspettato: " + operationType + ". In una sequenza di operazioni " +
                            "concorrenti sono ammessi solo: call, notify, exception, sleep");
            }
            //log.info("operation: " + operation.toString());
            listOfConcurrentOperations.add(operation);
        }

        /*
        // stampa di tutte le operazioni concorrenti generate
        log.info("operazioni concorrenti:");
        listOfConcurrentOperations.forEach( op -> {
            log.info("operazione:");
            op.forEach( element -> log.info(element + " "));
        });
        */

        if(stConcurrentOperations.hasMoreTokens())
            throw new MalformedStringOfOperationsException("errore di formattazione nella sequenza di operazioni concorrenti");

        templateService.executeConcurrentOperations(listOfConcurrentOperations);
    }
}
