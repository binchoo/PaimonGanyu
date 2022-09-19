package org.binchoo.paimonganyu.testconfig;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jbinchoo
 * @since 2022/07/02
 */
@RequiredArgsConstructor
@Component
public class TestUserRedeemTableSetup {

    private static final String USER_PREFIX = "testuser-";
    private static final int USER_COUNT = 26;
    private static final int NUM_DATA = 1000;

    private final AmazonDynamoDB dynamoClient;
    private DynamoDBMapper mapper;

    @PostConstruct
    public void createTable() {
        init();
        ddl(UserRedeemItem.class, 100L, 100L);
        dml(loadData(NUM_DATA));
    }

    @PreDestroy
    public void cleanDate() {
        dynamoClient.deleteTable(mapper.generateDeleteTableRequest(UserRedeemItem.class));
        System.out.println("Test deletion succeed.");
    }

    private void init() {
        this.mapper = new DynamoDBMapper(dynamoClient);
    }

    private void ddl(Class<?> itemClass, long rcu, long wcu) {
        CreateTableRequest request = mapper.generateCreateTableRequest(itemClass);
        request.setProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));
        try {
            dynamoClient.createTable(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<UserRedeemItem> loadData(int count) {
        return IntStream.range(0, count).mapToObj(it-> {
                String r = UUID.randomUUID().toString();
                int hashcode = r.hashCode();
                return UserRedeemItem.fromDomain(UserRedeem.builder()
                        .botUserId(USER_PREFIX + hashcode % USER_COUNT)
                        .uid("964073")// r + "uid"
                        .done(hashcode % 2 == 0)
                        .redeemCode(RedeemCode.of(r))
                        .date(LocalDateTime.now())
                        .build());
        }).collect(Collectors.toList());
    }

    public void dml(List<UserRedeemItem> data) {
        for (UserRedeemItem item : data)
            mapper.save(item);
    }
}
