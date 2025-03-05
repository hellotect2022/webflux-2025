package com.mpole.imp.api.database.repo;

import com.mpole.imp.api.database.entity.UserInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<UserInfo, Long> {
    //Flux<UserInfo> findBy
}
