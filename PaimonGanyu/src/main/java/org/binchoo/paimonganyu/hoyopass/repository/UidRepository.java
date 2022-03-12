package org.binchoo.paimonganyu.hoyopass.repository;

import org.binchoo.paimonganyu.hoyopass.entity.Uid;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface UidRepository extends CrudRepository<Uid, String> {
    Optional<Uid> findByUid(String uid);
}
