CREATE TABLE `registration` (
  `id` char(26) NOT NULL,
  `conference` char(26) NOT NULL,
  `food_choice` varchar(20) NOT NULL,
  `morning_topic` varchar(128) NOT NULL,
  `afternoon_topic` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (conference) REFERENCES conference(id),
  FOREIGN KEY (food_choice, conference) REFERENCES food(id, conference),
  FOREIGN KEY (morning_topic, conference) REFERENCES morning_topic(id, conference),
  FOREIGN KEY (afternoon_topic, conference) REFERENCES afternoon_topic(id, conference)
);