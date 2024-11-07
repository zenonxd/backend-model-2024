CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE event (
    /*
    get_random_uuid é uma função
    de uma extensão do Postgres
    onde iremos instalar depois.
    Ela irá gerar uma random UUID
    */
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(200) NOT NULL,
  img_url VARCHAR(100) NOT NULL,
  event_url VARCHAR(100) NOT NULL,
  date TIMESTAMP NOT NULL,
  remote BOOLEAN NOT NULL
);