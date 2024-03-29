package org.binchoo.paimonganyu.testconfig;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.infra.hoyopass.dynamo.item.UserHoyopassItem;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author jbinchoo
 * @since 2022/07/02
 */
@RequiredArgsConstructor
@Component
public class TestUserHoyopassTableSetup {

    private final AmazonDynamoDB dynamoClient;

    @PostConstruct
    public void createTable() {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
        CreateTableRequest request = mapper.generateCreateTableRequest(UserHoyopassItem.class);
        request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
        try {
            dynamoClient.createTable(request);
        } catch (Exception e) { 
            e.printStackTrace();
        }
        dynamoClient.listTables().getTableNames().forEach(System.out::println);
    }
}
