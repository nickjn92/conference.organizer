CREATE TABLE `afternoon_topic` (
  `id` varchar(128) NOT NULL,
  `conference` char(26) NOT NULL,
  PRIMARY KEY (`id`, `conference`),
  FOREIGN KEY (conference) REFERENCES conference(id)
);

INSERT INTO `afternoon_topic` VALUES
    ('PROMETHEUS 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('FLAGGER 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('ULID 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV');