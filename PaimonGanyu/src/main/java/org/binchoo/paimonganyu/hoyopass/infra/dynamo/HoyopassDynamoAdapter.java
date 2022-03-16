package org.binchoo.paimonganyu.hoyopass.infra.dynamo;

import org.binchoo.paimonganyu.hoyopass.domain.Hoyopass;
import org.binchoo.paimonganyu.hoyopass.domain.driven.HoyopassRepositoryPort;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface HoyopassDynamoAdapter extends HoyopassRepositoryPort, CrudRepository<Hoyopass, String> {
}
