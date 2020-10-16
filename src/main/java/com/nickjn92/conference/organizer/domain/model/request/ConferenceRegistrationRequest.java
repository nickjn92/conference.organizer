package com.nickjn92.conference.organizer.domain.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceRegistrationRequest {
  private String morningTopic;
  private String foodChoice;
  private String afternoonTopic;
}
