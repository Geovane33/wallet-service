CREATE TABLE IF NOT EXISTS wallet (
  id BIGINT AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  balance DECIMAL(10,2) DEFAULT NULL,
  active int DEFAULT 1,
  creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_update TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);