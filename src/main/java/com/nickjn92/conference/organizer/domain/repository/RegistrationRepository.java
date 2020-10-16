package com.nickjn92.conference.organizer.domain.repository;

import com.nickjn92.conference.organizer.domain.model.database.Registration;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RegistrationRepository extends R2dbcRepository<Registration, String> {

  Mono<Boolean> deleteByIdAndConference(String id, String conferenceId);
}
