package com.nickjn92.conference.organizer.service.validation;

import com.nickjn92.conference.organizer.domain.model.exception.BadRequestException;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import io.azam.ulidj.ULID;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class RegistrationValidation {

  private static final String CONFERENCE_ID_FIELD_NAME = "conferenceId";
  private static final String REGISTRATION_ID_FIELD_NAME = "registrationId";
  private static final String MORNING_TOPIC_FIELD_NAME = "morningTopic";
  private static final String AFTERNOON_TOPIC_FIELD_NAME = "afternoonTopic";
  private static final String FOOD_CHOICE_FIELD_NAME = "foodChoice";

  public Mono<ConferenceRegistrationRequest> validateRegisterRequest(
      String conferenceId, ConferenceRegistrationRequest request) {
    return validateUlid(CONFERENCE_ID_FIELD_NAME, conferenceId)
        .then(validateNotBlank(MORNING_TOPIC_FIELD_NAME, request.getMorningTopic()))
        .then(validateNotBlank(AFTERNOON_TOPIC_FIELD_NAME, request.getAfternoonTopic()))
        .then(validateNotBlank(FOOD_CHOICE_FIELD_NAME, request.getFoodChoice()))
        .thenReturn(request);
  }

  /*
   * Since update registration is a PATCH event, all fields could potentially be empty, then no
   * update will be done on the registration stored in the database
   */
  public Mono<ConferenceRegistrationRequest> validateUpdateRegistrationRequest(
      String conferenceId, String registrationId, ConferenceRegistrationRequest request) {
    return validateUlid(CONFERENCE_ID_FIELD_NAME, conferenceId)
        .then(validateUlid(REGISTRATION_ID_FIELD_NAME, registrationId))
        .thenReturn(request);
  }

  public Mono<Void> validateUnregisterRequest(String conferenceId, String registrationId) {
    return validateUlid(CONFERENCE_ID_FIELD_NAME, conferenceId)
        .then(validateUlid(REGISTRATION_ID_FIELD_NAME, registrationId));
  }

  private Mono<Void> validateUlid(String fieldName, String expectedUlid) {
    return Mono.just(expectedUlid)
        .filter(ULID::isValid)
        .switchIfEmpty(Mono.error(new BadRequestException(fieldName + " is not a valid id")))
        .then();
  }

  private Mono<Void> validateNotBlank(String fieldName, String fieldValue) {
    return Mono.just(Objects.requireNonNullElse(fieldValue, ""))
        .filter(Predicate.not(String::isBlank))
        .switchIfEmpty(Mono.error(new BadRequestException(fieldName + " can not be blank")))
        .then();
  }
}
