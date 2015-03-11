# urls schema

# --- !Ups

CREATE TABLE urls(urlid INT PRIMARY KEY AUTO_INCREMENT, url VARCHAR_IGNORECASE(255))

# --- !Downs

DROP TABLE urls;