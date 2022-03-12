package org.binchoo.paimonganyu.hoyopass.repository;

import org.binchoo.paimonganyu.hoyopass.entity.Hoyopass;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface HoyopassRepository extends CrudRepository<Hoyopass, String> {
    Optional<Hoyopass> findById(String id);
    Optional<List<Hoyopass>> findByUserId(String userId);
    Optional<Hoyopass> findByLtuid(String ltuid);
}
