package com.nickjn92.conference.organizer.domain.handler.impl;

import com.nickjn92.conference.organizer.domain.handler.ConferenceHandler;
import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.exception.NotFoundException;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import com.nickjn92.conference.organizer.domain.repository.RegistrationRepository;
import io.azam.ulidj.ULID;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class ConferenceHandlerImpl implements ConferenceHandler {

  private static final String REGISTRATION_NOT_FOUND =
      "Registration [%s] not found in conference [%s]";

  private final RegistrationRepository registrationRepository;

  public ConferenceHandlerImpl(final RegistrationRepository registrationRepository) {
    this.registrationRepository = registrationRepository;
  }

  @Override
  public Mono<Registration> register(String conferenceId, ConferenceRegistrationRequest request) {

    log.info(
        "Attempting to process registration to conference [{}] with choices [{}]",
        conferenceId,
        request);

    return mapRequestToRegistration(conferenceId, ULID.random(), request)
        .map(Registration::setAsNew)
        .flatMap(registrationRepository::save)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<Registration> updateRegistration(
      String conferenceId, String registrationId, ConferenceRegistrationRequest request) {

    log.info(
        "Attempting to update registration [{}] to conference [{}] with choices [{}]",
        registrationId,
        conferenceId,
        request);

    return registrationRepository
        .findById(registrationId)
        .switchIfEmpty(Mono.error(getRegistrationNotFound(registrationId, conferenceId)))
        .flatMap(
            dbRegistration ->
                mapRequestToRegistration(conferenceId, registrationId, request)
                    .map(dbRegistration::merge))
        .flatMap(registrationRepository::save)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<Void> deregister(String conferenceId, String registrationId) {

    log.info("Attempting to unregister registration [{}]", registrationId);

    return registrationRepository
        .deleteByIdAndConference(registrationId, conferenceId)
        .filter(wasDeleted -> wasDeleted)
        .switchIfEmpty(Mono.error(getRegistrationNotFound(registrationId, conferenceId)))
        .then()
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<Registration> mapRequestToRegistration(
      String conferenceId, String registrationId, ConferenceRegistrationRequest request) {
    Registration registration =
        Registration.builder()
            .id(Objects.requireNonNullElse(registrationId, "").toUpperCase())
            .conference(Objects.requireNonNullElse(conferenceId, "").toUpperCase())
            .foodChoice(Objects.requireNonNullElse(request.getFoodChoice(), "").toUpperCase())
            .afternoonTopic(
                Objects.requireNonNullElse(request.getAfternoonTopic(), "").toUpperCase())
            .morningTopic(Objects.requireNonNullElse(request.getMorningTopic(), "").toUpperCase())
            .build();
    return Mono.just(registration);
  }

  private NotFoundException getRegistrationNotFound(String registrationId, String conferenceId) {
    return new NotFoundException(
        String.format(REGISTRATION_NOT_FOUND, registrationId, conferenceId));
  }
}
