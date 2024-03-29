package org.binchoo.paimonganyu.infra.redeem.s3.repository;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.binchoo.paimonganyu.redeem.RedeemCode;
import org.binchoo.paimonganyu.redeem.driven.RedeemCodeCrudPort;

import java.util.List;

/**
 * @author : jbinchoo
 * @since : 2022-05-17
 */
//@Component unattached
@RequiredArgsConstructor
public class RedeemCodeS3Adapter implements RedeemCodeCrudPort {

    private final AmazonS3 s3Client;

    @Override
    public List<RedeemCode> findAll() {
        return null;
    }
}
