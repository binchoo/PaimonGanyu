package org.binchoo.paimonganyu.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.binchoo.paimonganyu.hoyopass.infra.dynamo.item.UserHoyopassItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("integ")
public class DynamoTableBuilder {

    @Autowired
    private AmazonDynamoDB dynamoClient;

    private DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

    @PostConstruct
    public void initTable() {
        CreateTableRequest request = mapper.generateCreateTableRequest(UserHoyopassItem.class);
        request.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

        try {
            dynamoClient.createTable(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
