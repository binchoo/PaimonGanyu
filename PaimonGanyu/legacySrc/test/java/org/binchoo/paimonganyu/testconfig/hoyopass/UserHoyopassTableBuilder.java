package org.binchoo.paimonganyu.testconfig.hoyopass;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.item.UserHoyopassItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserHoyopassTableBuilder {

    @Autowired
    private AmazonDynamoDB dynamoClient;

    @PostConstruct
    public void initTable() {
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
