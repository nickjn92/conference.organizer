package com.nickjn92.conference.organizer.domain.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.nickjn92.conference.organizer.domain.handler.impl.ConferenceHandlerImpl;
import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import com.nickjn92.conference.organizer.domain.repository.RegistrationRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class AbstractConferenceHandlerImplTest {

  private static final String ARBITRARY_CONFERENCE = "arbitrary-conference";
  private static final String ARBITRARY_REGISTRATION_ID = "arbitrary-conference";
  private static final ConferenceRegistrationRequest ARBITRARY_REGISTRATION_REQUEST =
      ConferenceRegistrationRequest.builder()
          .afternoonTopic(RandomString.make(10))
          .foodChoice(RandomString.make(10))
          .morningTopic(RandomString.make(10))
          .build();
  private static final Registration ARBITRARY_REGISTRATION =
      Registration.builder()
          .afternoonTopic(RandomString.make(10))
          .foodChoice(RandomString.make(10))
          .morningTopic(RandomString.make(10))
          .build();

  @Mock private RegistrationRepository registrationRepository;

  protected ConferenceHandler underTest;

  @BeforeEach
  public void setUp() {
    underTest = new ConferenceHandlerImpl(registrationRepository);
  }

  /*
   * Given
   */

  protected void givenDatabaseDown() {
    Mockito.lenient()
        .doReturn(Mono.error(new TransientDataAccessResourceException("database kaputski")))
        .when(registrationRepository)
        .save(any());
    Mockito.lenient()
        .doReturn(Mono.error(new TransientDataAccessResourceException("database kaputski")))
        .when(registrationRepository)
        .findById(anyString());
    Mockito.lenient()
        .doReturn(Mono.error(new TransientDataAccessResourceException("database kaputski")))
        .when(registrationRepository)
        .deleteByIdAndConference(any(), any());
  }

  protected void givenRepositorySavesSuccessfully() {
    Mockito.doAnswer(invocation -> Mono.just(invocation.getArgument(0)))
        .when(registrationRepository)
        .save(any());
  }

  protected void givenInvalidFields() {
    Mockito.doReturn(Mono.error(new DataIntegrityViolationException("field does not exist")))
        .when(registrationRepository)
        .save(any());
  }

  protected void givenRegistrationExistsInRepository() {
    Mockito.doReturn(Mono.just(ARBITRARY_REGISTRATION))
        .when(registrationRepository)
        .findById(anyString());
  }

  protected void givenRegistrationDoesNotExistsInRepository() {
    Mockito.doReturn(Mono.empty()).when(registrationRepository).findById(anyString());
  }

  protected void givenSuccessfullDelete() {
    Mockito.doReturn(Mono.just(true))
        .when(registrationRepository)
        .deleteByIdAndConference(any(), any());
  }

  protected void givenUnsuccessfullDelete() {
    Mockito.doReturn(Mono.just(false))
        .when(registrationRepository)
        .deleteByIdAndConference(any(), any());
  }

  /*
   * When
   */

  protected StepVerifier.FirstStep<Registration> whenRegistering() {
    return StepVerifier.create(
        underTest.register(ARBITRARY_CONFERENCE, ARBITRARY_REGISTRATION_REQUEST));
  }

  protected StepVerifier.FirstStep<Registration> whenUpdatingRegistration() {
    return StepVerifier.create(
        underTest.updateRegistration(
            ARBITRARY_CONFERENCE, ARBITRARY_REGISTRATION_ID, ARBITRARY_REGISTRATION_REQUEST));
  }

  protected StepVerifier.FirstStep<Void> whenUnregistering() {
    return StepVerifier.create(
        underTest.deregister(ARBITRARY_REGISTRATION_ID, ARBITRARY_REGISTRATION_ID));
  }
}
