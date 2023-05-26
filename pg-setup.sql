
--
-- Command to load this file:
--
-- psql -U neemapp -f neemapp/pg-setup.sql
--


CREATE TABLE IF NOT EXISTS patient (
  id bigserial primary key,
  name varchar(128) unique
);

CREATE TABLE IF NOT EXISTS insurance_plan (
  id bigserial primary key,
  name varchar(128) unique,
  description varchar(256),
  deductible int,
  overrides varchar(256)
);

CREATE TABLE IF NOT EXISTS subscription (
  id bigserial primary key,
  patient_id bigint references patient NOT NULL,
  plan_id bigint references insurance_plan NOT NULL,
  used_deductible int,
  used_overrides varchar(256)
);


INSERT INTO patient (name)
 SELECT 'Jack'
 WHERE NOT EXISTS (SELECT name FROM patient WHERE name = 'Jack')
;
INSERT INTO patient (name)
 SELECT 'Jill'
 WHERE NOT EXISTS (SELECT name FROM patient WHERE name = 'Jill')
;
INSERT INTO patient (name)
 SELECT 'John'
 WHERE NOT EXISTS (SELECT name FROM patient WHERE name = 'John')
;


INSERT INTO insurance_plan (name, description, deductible, overrides)
 SELECT 'Plan1', 'Plan1', 300, ''
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan1')
;
INSERT INTO insurance_plan (name, description, deductible, overrides)
 SELECT 'Plan2', 'Plan2', 300, ''
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan2')
;
INSERT INTO insurance_plan (name, description, deductible, overrides)
 SELECT 'Plan3', 'Plan3', 300, ''
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan3')
;

INSERT INTO subscription (patient_id, plan_id, used_deductible, used_overrides)
 SELECT p.id, i.id, 0, ''
   FROM patient p
   JOIN insurance_plan i on i.id >= p.id
 WHERE NOT EXISTS (SELECT patient_id FROM subscription WHERE patient_id = p.id)
;



CREATE TABLE IF NOT EXISTS neem_user (
  id bigserial primary key,
  name varchar(128),
  role varchar(128)
);

CREATE INDEX IF NOT EXISTS neem_user_idx_name ON neem_user(name) ;


INSERT INTO neem_user (name, role)
 SELECT 'abc', 'def'
 WHERE NOT EXISTS (SELECT name FROM neem_user WHERE name = 'abc')
;

INSERT INTO neem_user (name, role)
 SELECT 'ghi', 'jkl'
 WHERE NOT EXISTS (SELECT name FROM neem_user WHERE name = 'ghi')
;



--
--  id | name | role 
-- ----+------+------
--   1 | abc  | def
--   2 | ghi  | jkl
-- (2 rows)

-- neemapp=> \d neem_user
--
-- \c neemapp neemapp
--
