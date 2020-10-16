package com.nickjn92.conference.organizer.integration;

import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import io.azam.ulidj.ULID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest {

  protected static final String EXISTING_MORNING_TOPIC = "KUBERNETES 101";
  protected static final String OTHER_EXISTING_MORNING_TOPIC = "ISTIO 101";
  protected static final String EXISTING_AFTERNOON_TOPIC = "FLAGGER 101";
  protected static final String OTHER_EXISTING_AFTERNOON_TOPIC = "PROMETHEUS 101";

  protected static final String NON_EXISTING_MORNING_TOPIC = "MS-DOS 101";
  protected static final String NON_EXISTING_AFTERNOON_TOPIC = "MS-DOS 102";

  protected static final String VALID_FOOD_CHOICE = "MEAT";
  protected static final String OTHER_VALID_FOOD_CHOICE = "FISH";
  protected static final String INVALID_FOOD_CHOICE = "HUMAN";

  protected static final String EXISTING_CONFERENCE_ID = "01ARZ3NDEKTSV4RRFFQ69G5FAV";
  protected static final String NON_EXISTING_CONFERENCE_ID = ULID.random();
  protected static final String INVALID_CONFERENCE_ID = "non-existing-conference-id";

  protected static final String INVALID_REGISTRATION_ID = "invalid-registration-id";

  private static final String REGISTER_URI = "/v1/conference/{conference}/registration";
  private static final String UPDATE_URI =
      "/v1/conference/{conference}/registration/{registration}";
  private static final String DELETE_URI = UPDATE_URI;

  private static WebTestClient webClient;

  @BeforeAll
  public static void setup(@LocalServerPort int port) {
    webClient =
        WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
  }

  protected WebTestClient.ResponseSpec whenSendingRegisterRequest(
      String conferenceId, ConferenceRegistrationRequest request) {
    return webClient.post().uri(REGISTER_URI, conferenceId).bodyValue(request).exchange();
  }

  protected Registration doAndAssertValidRegistration() {
    var request =
        givenConferenceRegistrationRequest(
            EXISTING_MORNING_TOPIC, EXISTING_AFTERNOON_TOPIC, VALID_FOOD_CHOICE);
    return whenSendingRegisterRequest(EXISTING_CONFERENCE_ID, request)
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(Registration.class)
        .returnResult()
        .getResponseBody();
  }

  protected WebTestClient.ResponseSpec whenSendingUpdateRegistrationRequest(
      String conferenceId, String registrationId, ConferenceRegistrationRequest request) {
    return webClient
        .patch()
        .uri(UPDATE_URI, conferenceId, registrationId)
        .bodyValue(request)
        .exchange();
  }

  protected WebTestClient.ResponseSpec whenSendingUnregisterRequest(
      String conferenceId, String registrationId) {
    return webClient.delete().uri(DELETE_URI, conferenceId, registrationId).exchange();
  }

  protected ConferenceRegistrationRequest givenConferenceRegistrationRequest(
      String morningTopic, String afternoonTopic, String foodChoice) {
    return ConferenceRegistrationRequest.builder()
        .morningTopic(morningTopic)
        .foodChoice(foodChoice)
        .afternoonTopic(afternoonTopic)
        .build();
  }

  protected void assertExpectedRegistration(
      String expectedId,
      String expectedMorningTopic,
      String expectedAfternoonTopic,
      String expectedFoodChoice,
      Registration actualRegistration) {
    Assertions.assertEquals(expectedMorningTopic, actualRegistration.getMorningTopic());
    Assertions.assertEquals(expectedAfternoonTopic, actualRegistration.getAfternoonTopic());
    Assertions.assertEquals(expectedFoodChoice, actualRegistration.getFoodChoice());
    // For update requests we know which identifier we expect to be returned
    if (expectedId != null) {
      Assertions.assertEquals(expectedId, actualRegistration.getId());
    } else {
      // Else its a newly created registration, expect a valid ULID identifier
      Assertions.assertTrue(ULID.isValid(actualRegistration.getId()));
    }
  }
}
