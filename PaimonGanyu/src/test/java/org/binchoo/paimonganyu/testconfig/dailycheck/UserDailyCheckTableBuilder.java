package org.binchoo.paimonganyu.testconfig.dailycheck;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.binchoo.paimonganyu.dailycheck.domain.UserDailyCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class UserDailyCheckTableBuilder {

    @Autowired
    private AmazonDynamoDB dynamoClient;

    private ProvisionedThroughput provisionedThroughput =
            new ProvisionedThroughput(5L, 5L);

    @PostConstruct
    public void initTable() {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);

        CreateTableRequest request = mapper.generateCreateTableRequest(UserDailyCheck.class);
        request.setProvisionedThroughput(provisionedThroughput);
        request.setKeySchema(Collections.singletonList(new KeySchemaElement("id", "HASH")));
        request.setGlobalSecondaryIndexes(Collections.singletonList(
                new GlobalSecondaryIndex()
                        .withIndexName("botUserIdLtuid")
                        .withProjection(new Projection().withProjectionType("ALL"))
                        .withKeySchema(
                            new KeySchemaElement("botUserIdLtuid", "HASH"))
                        .withProvisionedThroughput(provisionedThroughput)
        ));

        try {
            dynamoClient.createTable(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
