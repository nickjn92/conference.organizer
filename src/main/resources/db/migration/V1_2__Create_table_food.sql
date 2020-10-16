CREATE TABLE `food` (
  `id` varchar(15) NOT NULL,
  `conference` char(26) NOT NULL,
  PRIMARY KEY (`id`, `conference`),
  FOREIGN KEY (conference) REFERENCES conference(id)
);

INSERT INTO `food` VALUES
    ('MEAT', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('FISH', '01ARZ3NDEKTSV4RRFFQ69G5FAV'),
    ('VEGAN', '01ARZ3NDEKTSV4RRFFQ69G5FAV');
