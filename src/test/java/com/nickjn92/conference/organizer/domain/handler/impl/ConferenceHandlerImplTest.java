package com.nickjn92.conference.organizer.domain.handler.impl;

import com.nickjn92.conference.organizer.domain.handler.AbstractConferenceHandlerImplTest;
import com.nickjn92.conference.organizer.domain.model.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;

@ExtendWith(MockitoExtension.class)
class ConferenceHandlerImplTest extends AbstractConferenceHandlerImplTest {

  /*
   * Registration Tests
   */

  @Test
  void register() {
    givenRepositorySavesSuccessfully();
    whenRegistering().expectNextCount(1).verifyComplete();
  }

  @Test
  void register_InvalidFields() {
    givenInvalidFields();
    whenRegistering().verifyError(DataIntegrityViolationException.class);
  }

  @Test
  void register_DatabaseDown() {
    givenDatabaseDown();
    whenRegistering().verifyError(TransientDataAccessResourceException.class);
  }

  /*
   * Patch
   */

  @Test
  void patch() {
    givenRegistrationExistsInRepository();
    givenRepositorySavesSuccessfully();

    whenUpdatingRegistration().expectNextCount(1).verifyComplete();
  }

  @Test
  void patch_RegistrationDoesNotExist() {
    givenRegistrationDoesNotExistsInRepository();

    whenUpdatingRegistration().verifyError(NotFoundException.class);
  }

  @Test
  void patch_InvalidFields() {
    givenRegistrationExistsInRepository();
    givenInvalidFields();

    whenUpdatingRegistration().verifyError(DataIntegrityViolationException.class);
  }

  @Test
  void patch_DatabaseDown() {
    givenDatabaseDown();
    whenUpdatingRegistration().verifyError(TransientDataAccessResourceException.class);
  }

  /*
   * Delete
   */

  @Test
  void delete() {
    givenSuccessfullDelete();
    whenUnregistering().verifyComplete();
  }

  @Test
  void delete_NonExistingRegistrationId() {
    givenUnsuccessfullDelete();
    whenUnregistering().verifyError(NotFoundException.class);
  }

  @Test
  void delete_DatabaseDown() {
    givenDatabaseDown();
    whenUnregistering().verifyError(TransientDataAccessResourceException.class);
  }
}
