package com.nickjn92.conference.organizer.integration;

import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import io.azam.ulidj.ULID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UpdateRegistrationTests extends BaseIntegrationTest {

  private Registration whenSendingUpdateRegistrationRequestExpectStatus(
      String conferenceId,
      String registrationId,
      ConferenceRegistrationRequest request,
      HttpStatus expectedStatus) {
    var responseSpec =
        whenSendingUpdateRegistrationRequest(conferenceId, registrationId, request)
            .expectStatus()
            .isEqualTo(expectedStatus);

    // If we expect a valid result we want to do assertions on the result in some tests
    if (expectedStatus == HttpStatus.OK) {
      return responseSpec.expectBody(Registration.class).returnResult().getResponseBody();
    }
    return null;
  }

  @Test
  void updateRegistration_ValidRequest() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.OK);
  }

  @Test
  void updateRegistration_NotAllFieldsSet() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(OTHER_EXISTING_MORNING_TOPIC, null, null);
    Registration updatedRegistration =
        whenSendingUpdateRegistrationRequestExpectStatus(
            EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.OK);

    assert updatedRegistration != null;
    // The patch request only changed the morning topic
    assertExpectedRegistration(
        registration.getId(),
        OTHER_EXISTING_MORNING_TOPIC,
        registration.getAfternoonTopic(),
        registration.getFoodChoice(),
        updatedRegistration);
  }

  @Test
  void updateRegistration_EmptyRequest() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest = givenConferenceRegistrationRequest(null, null, null);
    Registration updatedRegistration =
        whenSendingUpdateRegistrationRequestExpectStatus(
            EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.OK);
    assert updatedRegistration != null;
    // The patch changed no fields
    assertExpectedRegistration(
        registration.getId(),
        registration.getMorningTopic(),
        registration.getAfternoonTopic(),
        registration.getFoodChoice(),
        updatedRegistration);
  }

  /*
   * Expect BAD_REQUEST back
   */
  @Test
  void updateRegistration_InvalidConferenceId() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        INVALID_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.BAD_REQUEST);
  }

  /*
   * Expect NOT_FOUND back
   */

  @Test
  void updateRegistration_NonExistingConferenceId() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        NON_EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.OK);
  }

  @Test
  void updateRegistration_NonExistingRegistrationId() {
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        EXISTING_CONFERENCE_ID, ULID.random(), updateRequest, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_InvalidMorningTopic() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            NON_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_InvalidAfternoonTopic() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, NON_EXISTING_AFTERNOON_TOPIC, OTHER_VALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_InvalidFoodChoice() {
    Registration registration = doAndAssertValidRegistration();
    var updateRequest =
        givenConferenceRegistrationRequest(
            OTHER_EXISTING_MORNING_TOPIC, OTHER_EXISTING_AFTERNOON_TOPIC, INVALID_FOOD_CHOICE);
    whenSendingUpdateRegistrationRequestExpectStatus(
        EXISTING_CONFERENCE_ID, registration.getId(), updateRequest, HttpStatus.NOT_FOUND);
  }
}
