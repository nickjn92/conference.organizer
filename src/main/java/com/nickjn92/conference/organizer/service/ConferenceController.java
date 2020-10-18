package com.nickjn92.conference.organizer.service;

import com.nickjn92.conference.organizer.domain.handler.ConferenceHandler;
import com.nickjn92.conference.organizer.domain.model.database.Registration;
import com.nickjn92.conference.organizer.domain.model.exception.BadRequestException;
import com.nickjn92.conference.organizer.domain.model.request.ConferenceRegistrationRequest;
import com.nickjn92.conference.organizer.service.validation.RegistrationValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(
    value = "/v1/conference/{conference-id}",
    consumes = MediaType.APPLICATION_JSON_VALUE)
public class ConferenceController {

  private final ConferenceHandler conferenceHandler;

  public ConferenceController(final ConferenceHandler conferenceHandler) {
    this.conferenceHandler = conferenceHandler;
  }

  @PostMapping("/registration")
  public Mono<Registration> register(
      @PathVariable("conference-id") String conferenceId,
      @RequestBody ConferenceRegistrationRequest request) {
    return RegistrationValidation.validateRegisterRequest(conferenceId, request)
        .flatMap(validatedRequest -> conferenceHandler.register(conferenceId, validatedRequest));
  }

  @PatchMapping("/registration/{registration-id}")
  public Mono<Registration> changeChoices(
      @PathVariable("conference-id") String conferenceId,
      @PathVariable("registration-id") String registrationId,
      @RequestBody ConferenceRegistrationRequest request) {
    return RegistrationValidation.validateUpdateRegistrationRequest(
            conferenceId, registrationId, request)
        .flatMap(
            validatedRequest ->
                conferenceHandler.updateRegistration(
                    conferenceId, registrationId, validatedRequest));
  }

  @DeleteMapping("/registration/{registration-id}")
  public Mono<Void> deregister(
      @PathVariable("conference-id") String conferenceId,
      @PathVariable("registration-id") String registrationId) {
    return RegistrationValidation.validateDeregisterRequest(conferenceId, registrationId)
        .then(conferenceHandler.deregister(conferenceId, registrationId));
  }

  /*
   * Explicit mapping for when path variable is empty or null, otherwise Spring maps this to 404 not found
   */
  @RequestMapping("/registration/")
  public Mono<Void> invalidMapping() {
    return Mono.error(new BadRequestException("conferenceId in url path can not be empty"));
  }
}
