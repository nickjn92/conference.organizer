package com.nickjn92.conference.organizer.integration;

import com.nickjn92.conference.organizer.domain.model.database.Registration;
import io.azam.ulidj.ULID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class DeleteRegistrationTests extends BaseIntegrationTest {

  private void whenSendingUnregisterRequestExpectStatus(
      String conferenceId, String registrationId, HttpStatus expectedStatus) {
    whenSendingUnregisterRequest(conferenceId, registrationId)
        .expectStatus()
        .isEqualTo(expectedStatus);
  }

  @Test
  void delete_ValidRequest() {
    Registration registration = doAndAssertValidRegistration();
    whenSendingUnregisterRequestExpectStatus(
        EXISTING_CONFERENCE_ID, registration.getId(), HttpStatus.OK);
  }

  /*
   * Expect BAD_REQUEST
   */

  @Test
  void delete_invalidConferenceId() {
    Registration registration = doAndAssertValidRegistration();
    whenSendingUnregisterRequestExpectStatus(
        INVALID_CONFERENCE_ID, registration.getId(), HttpStatus.BAD_REQUEST);
  }

  @Test
  void delete_MissingRegistrationId() {
    whenSendingUnregisterRequestExpectStatus(EXISTING_CONFERENCE_ID, "", HttpStatus.BAD_REQUEST);
  }

  @Test
  void delete_InvalidRegistrationId() {
    whenSendingUnregisterRequestExpectStatus(
        EXISTING_CONFERENCE_ID, INVALID_REGISTRATION_ID, HttpStatus.BAD_REQUEST);
  }

  /*
   * Expect NOT_FOUND
   */

  @Test
  void delete_NonExistingConferenceId() {
    Registration registration = doAndAssertValidRegistration();
    whenSendingUnregisterRequestExpectStatus(
        NON_EXISTING_CONFERENCE_ID, registration.getId(), HttpStatus.NOT_FOUND);
  }

  @Test
  void delete_NonExistingRegistrationId() {
    whenSendingUnregisterRequestExpectStatus(
        EXISTING_CONFERENCE_ID, ULID.random(), HttpStatus.NOT_FOUND);
  }
}
