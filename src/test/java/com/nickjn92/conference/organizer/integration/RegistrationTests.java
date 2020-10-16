package com.nickjn92.conference.organizer.integration;

import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RegistrationTests extends BaseIntegrationTest {

  @Test
  void register_ValidRequest() {
    doAndAssertValidRegistration();
  }

  private void whenSendingRegisterRequestExpectResponse(
      String conferenceId, ConferenceRegistrationRequest request, HttpStatus expectedStatus) {
    whenSendingRegisterRequest(conferenceId, request).expectStatus().isEqualTo(expectedStatus);
  }

  /*
   * Expect BAD_REQUEST
   */

  @Test
  void register_InvalidConferenceId() {
    var request =
        givenConferenceRegistrationRequest(
            EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(
        INVALID_CONFERENCE_ID, request, HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_MissingMorningTopic() {
    var request =
        givenConferenceRegistrationRequest(null, EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(
        EXISTING_CONFERENCE_ID, request, HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_MissingAfternoonTopic() {
    var request =
        givenConferenceRegistrationRequest(EXISTING_MORNING_TOPIC, null, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(
        EXISTING_CONFERENCE_ID, request, HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_MissingFoodChoice() {
    var request =
        givenConferenceRegistrationRequest(EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, null);
    whenSendingRegisterRequestExpectResponse(
        EXISTING_CONFERENCE_ID, request, HttpStatus.BAD_REQUEST);
  }

  /*
   * Expect NOT_FOUND
   */

  @Test
  void register_InvalidMorningTopic() {
    var request =
        givenConferenceRegistrationRequest(
            NON_EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(EXISTING_CONFERENCE_ID, request, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_InvalidAfternoonTopic() {
    var request =
        givenConferenceRegistrationRequest(
            EXISTING_MORNING_TOPIC, NON_EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(EXISTING_CONFERENCE_ID, request, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_InvalidFoodChoice() {
    var request =
        givenConferenceRegistrationRequest(
            EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, INVALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(EXISTING_CONFERENCE_ID, request, HttpStatus.NOT_FOUND);
  }

  @Test
  void register_NonExistingConferenceId() {
    var request =
        givenConferenceRegistrationRequest(
            EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    whenSendingRegisterRequestExpectResponse(
        NON_EXISTING_CONFERENCE_ID, request, HttpStatus.NOT_FOUND);
  }
}
