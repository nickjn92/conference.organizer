CREATE TABLE `morning_topic` (
  `id` varchar(128) NOT NULL,
  `conference` char(26) NOT NULL,
  PRIMARY KEY (`id`, `conference`),
  FOREIGN KEY (conference) REFERENCES conference(id)
);

INSERT INTO `morning_topic` VALUES
    ('KUBERNETES 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('ISTIO 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('JAEGER 101', '01ARZ3NDEKTSV4RRFFQ69G5FAV');