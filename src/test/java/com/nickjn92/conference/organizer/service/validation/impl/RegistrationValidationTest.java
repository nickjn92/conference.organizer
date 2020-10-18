package com.nickjn92.conference.organizer.service.validation.impl;

import com.nickjn92.conference.organizer.service.validation.AbstractRegistrationValidationTest;
import com.nickjn92.conference.organizer.service.validation.RegistrationValidation;
import org.junit.jupiter.api.Test;

class RegistrationValidationTest extends AbstractRegistrationValidationTest {

  /*
   * Registration Validation
   */

  @Test
  void register_ValidRequest() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertSuccessfullValidation(
        RegistrationValidation.validateRegisterRequest(VALID_CONFERENCE_ID, registrationRequest),
        true);
  }

  @Test
  void register_InvalidConferenceId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateRegisterRequest(INVALID_CONFERENCE_ID, registrationRequest));
  }

  @Test
  void register_MissingConferenceId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(RegistrationValidation.validateRegisterRequest("", registrationRequest));
  }

  @Test
  void register_MissingMorningTopic() {
    var registrationRequest =
        givenRegistrationRequest(null, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateRegisterRequest(VALID_CONFERENCE_ID, registrationRequest));
  }

  @Test
  void register_MissingAfternoonTopic() {
    var registrationRequest =
        givenRegistrationRequest(ARBITRARY_MORNING_TOPIC, null, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateRegisterRequest(VALID_CONFERENCE_ID, registrationRequest));
  }

  @Test
  void register_MissingFoodChoice() {
    var registrationRequest =
        givenRegistrationRequest(ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, null);
    assertFailedValidation(
        RegistrationValidation.validateRegisterRequest(VALID_CONFERENCE_ID, registrationRequest));
  }

  /*
   * Update registration validation
   */

  @Test
  void updateRegistration_ValidRequest_AllFieldsPresent() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertSuccessfullValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            VALID_CONFERENCE_ID, VALID_REGISTRATION_ID, registrationRequest),
        true);
  }

  @Test
  void updateRegistration_ValidRequest_SomeFieldsPresent() {
    var registrationRequest = givenRegistrationRequest(null, null, ARBITRARY_FOOD_CHOICE);
    assertSuccessfullValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            VALID_CONFERENCE_ID, VALID_REGISTRATION_ID, registrationRequest),
        true);
  }

  @Test
  void updateRegistration_ValidRequest_EmptyFields() {
    var registrationRequest = givenRegistrationRequest(null, null, null);
    assertSuccessfullValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            VALID_CONFERENCE_ID, VALID_REGISTRATION_ID, registrationRequest),
        true);
  }

  @Test
  void updateRegistration_InvalidConferenceId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            INVALID_CONFERENCE_ID, VALID_REGISTRATION_ID, registrationRequest));
  }

  @Test
  void updateRegistration_InvalidRegistrationId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            VALID_CONFERENCE_ID, INVALID_REGISTRATION_ID, registrationRequest));
  }

  @Test
  void updateRegistration_MissingConferenceId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            null, VALID_REGISTRATION_ID, registrationRequest));
  }

  @Test
  void updateRegistration_MissingRegistrationId() {
    var registrationRequest =
        givenRegistrationRequest(
            ARBITRARY_MORNING_TOPIC, ARBITRARY_AFTERNOON_TOPIC, ARBITRARY_FOOD_CHOICE);
    assertFailedValidation(
        RegistrationValidation.validateUpdateRegistrationRequest(
            VALID_CONFERENCE_ID, null, registrationRequest));
  }

  /*
   * Deregister validation
   */
  @Test
  void deregister_ValidRequest() {
    assertSuccessfullValidation(
        RegistrationValidation.validateDeregisterRequest(
            VALID_CONFERENCE_ID, VALID_REGISTRATION_ID),
        false);
  }

  @Test
  void deregister_InvalidConferenceId() {
    assertFailedValidation(
        RegistrationValidation.validateDeregisterRequest(
            INVALID_CONFERENCE_ID, VALID_REGISTRATION_ID));
  }

  @Test
  void deregister_InvalidRegistrationId() {
    assertFailedValidation(
        RegistrationValidation.validateDeregisterRequest(
            VALID_CONFERENCE_ID, INVALID_REGISTRATION_ID));
  }

  @Test
  void deregister_MissingConferenceId() {
    assertFailedValidation(
        RegistrationValidation.validateDeregisterRequest(null, VALID_REGISTRATION_ID));
  }

  @Test
  void deregister_MissingRegistrationId() {
    assertFailedValidation(
        RegistrationValidation.validateDeregisterRequest(VALID_CONFERENCE_ID, null));
  }
}
