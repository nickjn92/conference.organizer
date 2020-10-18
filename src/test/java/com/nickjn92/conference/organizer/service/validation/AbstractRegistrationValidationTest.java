package com.nickjn92.conference.organizer.service.validation;

import com.nickjn92.conference.organizer.domain.model.exception.BadRequestException;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import io.azam.ulidj.ULID;
import org.reactivestreams.Publisher;
import reactor.test.StepVerifier;

public class AbstractRegistrationValidationTest {
  protected static final String VALID_CONFERENCE_ID = ULID.random();
  protected static final String INVALID_CONFERENCE_ID = "not-ulid-conferece-id";

  protected static final String VALID_REGISTRATION_ID = ULID.random();

  protected static final String INVALID_REGISTRATION_ID = "not-ulid-registration-id";
  protected static final String ARBITRARY_MORNING_TOPIC = "Istio 101";
  protected static final String ARBITRARY_AFTERNOON_TOPIC = "ULID 101";
  protected static final String ARBITRARY_FOOD_CHOICE = "MEAT";

  protected ConferenceRegistrationRequest givenRegistrationRequest(
      String morningTopic, String afternoonTopic, String foodChoice) {
    return ConferenceRegistrationRequest.builder()
        .morningTopic(morningTopic)
        .afternoonTopic(afternoonTopic)
        .foodChoice(foodChoice)
        .build();
  }

  protected void assertSuccessfullValidation(Publisher<?> publisher, boolean expectOnNext) {
    StepVerifier.create(publisher).expectNextCount(expectOnNext ? 1 : 0).verifyComplete();
  }

  /*
   * This could check the contents of the exception for expected error fields if a proper validation
   * error model was used
   */
  protected void assertFailedValidation(Publisher<?> publisher) {
    StepVerifier.create(publisher).verifyError(BadRequestException.class);
  }
}
