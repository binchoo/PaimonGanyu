package org.binchoo.paimonganyu.infra.redeem.dynamo.item;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import org.binchoo.paimonganyu.infra.utils.LocalDateTimeStringConverter;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.UserRedeem;

import java.time.LocalDateTime;

import static org.binchoo.paimonganyu.infra.redeem.dynamo.item.UserRedeemItem.TABLE_NAME;

/**
 * @author : jbinchoo
 * @since : 2022-04-19
 */
@ToString
@Setter // required for conversion and unconversion, never remove this.
@Getter
@Builder
@AllArgsConstructor
@DynamoDBTable(tableName = TABLE_NAME)
public class UserRedeemItem {

    public static final String TABLE_NAME = "UserRedeem";

    @DynamoDBHashKey
    private String botUserId;

    @DynamoDBAttribute
    private String ltuid;

    @DynamoDBAttribute
    private String code;

    @DynamoDBTypeConvertedEnum
    private UserRedeemStatus status;

    @DynamoDBConvertedBool(DynamoDBConvertedBool.Format.true_false)
    private boolean done;

    @DynamoDBTypeConverted(converter = LocalDateTimeStringConverter.class)
    private LocalDateTime createAt;

    public UserRedeemItem() { }

    public static UserRedeem toDomain(UserRedeemItem userRedeemItem) {
        return new UserRedeem(userRedeemItem.botUserId,
                userRedeemItem.ltuid, new RedeemCode(userRedeemItem.code), userRedeemItem.isDone());
    }

    public static UserRedeemItem fromDomain(UserRedeem userRedeem) {
        return UserRedeemItem.builder()
                .botUserId(userRedeem.getBotUserId())
                .ltuid(userRedeem.getLtuid())
                .code(userRedeem.getRedeemCode().getCode())
                .status(null)
                .done(userRedeem.isDone())
                .build();
    }
}
