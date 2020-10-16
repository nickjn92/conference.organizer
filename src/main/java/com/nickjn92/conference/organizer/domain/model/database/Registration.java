package com.nickjn92.conference.organizer.domain.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Registration implements Persistable<String> {
  @Id private String id;
  private String conference;
  private String foodChoice;
  private String morningTopic;
  private String afternoonTopic;

  /* Helper methods to when using the ReactiveCrudRepositories `save` method.
   * Persistable uses the isNew() to determine wether to insert or update a entity.
   * Calling `setAsNew` would then tell the repository use save instead of default update.
   * @Transient is used to tell the repository to not look for column NEW_REGISTRATION and IS_NEW.
   */

  @JsonIgnore @Transient private boolean newRegistration;

  @JsonIgnore
  @Override
  @Transient
  public boolean isNew() {
    return this.newRegistration || id == null;
  }

  public Registration setAsNew() {
    this.newRegistration = true;
    return this;
  }

  public Registration merge(Registration other) {
    this.foodChoice = Strings.isBlank(other.foodChoice) ? this.foodChoice : other.foodChoice;
    this.morningTopic =
        Strings.isBlank(other.morningTopic) ? this.morningTopic : other.morningTopic;
    this.afternoonTopic =
        Strings.isBlank(other.afternoonTopic) ? this.afternoonTopic : other.afternoonTopic;
    return this;
  }
}
