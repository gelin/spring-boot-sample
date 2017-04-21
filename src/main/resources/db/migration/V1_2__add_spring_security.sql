create table users (
  username varchar(256),
  password varchar(256),
  enabled boolean
);

create table authorities (
  username  VARCHAR(256),
  authority VARCHAR(256)
);
