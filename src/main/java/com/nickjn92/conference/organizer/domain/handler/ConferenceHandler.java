package com.nickjn92.conference.organizer.domain.handler;

import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import reactor.core.publisher.Mono;

public interface ConferenceHandler {

  Mono<Registration> register(String conferenceId, ConferenceRegistrationRequest request);

  Mono<Registration> updateRegistration(
      String conferenceId, String registrationId, ConferenceRegistrationRequest request);

  Mono<Void> deregister(String conferenceId, String registrationId);
}
