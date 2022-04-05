package org.binchoo.paimonganyu.dailycheck.infra;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBEntityInformation;
import org.socialsignin.spring.data.dynamodb.repository.support.EnableScanPermissions;
import org.socialsignin.spring.data.dynamodb.repository.support.SimpleDynamoDBCrudRepository;

public class UserDailyCheckDynamoRepository extends SimpleDynamoDBCrudRepository {

    public UserDailyCheckDynamoRepository(DynamoDBEntityInformation entityInformation,
                                          DynamoDBOperations dynamoDBOperations,
                                          EnableScanPermissions enableScanPermissions) {
        super(entityInformation, dynamoDBOperations, enableScanPermissions);
    }
}
