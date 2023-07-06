
--
-- Command to load this file:
--
-- psql -U neemapp -f neemapp/pg-setup.sql
--


CREATE TABLE IF NOT EXISTS hashtest (
  emp_id uuid NOT NULL ,
  emp_name varchar(128) NOT NULL ,
  emp_hash integer NOT NULL ,
  PRIMARY KEY (emp_id, emp_hash) )
 PARTITION BY list( emp_hash ) ;
-- PARTITION BY hash(hashtext( cast( emp_id as text ))) ;

-- SELECT ('x' || lpad(md5sum(substr(cast( emp_id as varchar ), 12, 4)), 8, '0'))::bit(32)::int AS int_val
-- SELECT ('x' || lpad(hex, 8, '0'))::bit(32)::int AS int_val


CREATE TABLE IF NOT EXISTS hashtest_0 PARTITION OF hashtest DEFAULT ;
CREATE TABLE IF NOT EXISTS hashtest_1 PARTITION OF hashtest FOR VALUES IN (1, 5, 9, 13);
CREATE TABLE IF NOT EXISTS hashtest_2 PARTITION OF hashtest FOR VALUES IN (2, 6, 10, 14);
CREATE TABLE IF NOT EXISTS hashtest_3 PARTITION OF hashtest FOR VALUES IN (3, 7, 11, 15);


DO $$
BEGIN
IF NOT EXISTS ( SELECT conname, oid, conrelid FROM pg_constraint WHERE conname = 'hashtest_check_emp' ) THEN
  EXECUTE 'ALTER TABLE hashtest ADD CONSTRAINT hashtest_check_emp CHECK( (''x0000'' || right(cast( emp_id as varchar ), 4))::bit(32)::int % 16 = emp_hash )' ;
  INSERT INTO hashtest ( emp_id, emp_name, emp_hash ) SELECT id, name , ('x0000' || right(cast( id as varchar ), 4))::bit(32)::int % 16 from insurance_plan ;
END IF ;
END $$ ;



CREATE TABLE IF NOT EXISTS patient (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  created TIMESTAMP DEFAULT NOW(),
  modified TIMESTAMP DEFAULT NOW(),
  name varchar(128) unique
);

CREATE TABLE IF NOT EXISTS insurance_plan (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  created TIMESTAMP DEFAULT NOW(),
  modified TIMESTAMP DEFAULT NOW(),
  plan_start_date DATE,
  plan_end_date DATE,
  name varchar(128) unique,
  description varchar(256),
  deductible int,
  overrides jsonb
);

CREATE TABLE IF NOT EXISTS subscription (
  patient_id uuid references patient NOT NULL,
  plan_id uuid references insurance_plan NOT NULL,
  created TIMESTAMP DEFAULT NOW(),
  modified TIMESTAMP DEFAULT NOW(),
  coverage_start_date DATE,
  coverage_end_date DATE,
  used_deductible int,
  used_overrides jsonb,
  PRIMARY KEY (patient_id, plan_id)
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
 SELECT 'Plan1', 'Plan1', 300, null
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan1')
;
INSERT INTO insurance_plan (name, description, deductible, overrides)
 SELECT 'Plan2', 'Plan2', 300, null
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan2')
;
INSERT INTO insurance_plan (name, description, deductible, overrides)
 SELECT 'Plan3', 'Plan3', 300, null
 WHERE NOT EXISTS (SELECT name FROM insurance_plan WHERE name = 'Plan3')
;

INSERT INTO subscription (patient_id, plan_id, used_deductible, used_overrides)
 SELECT p.id, i.id, 0, null
   FROM patient p , insurance_plan i
 WHERE NOT EXISTS (SELECT patient_id FROM subscription WHERE patient_id = p.id)
   AND ( ( i.name = 'Plan3' AND p.name in ( 'Jack', 'Jill', 'John' ) )
        OR ( i.name = 'Plan2' AND p.name in ( 'Jill', 'John' ) )
        OR ( i.name = 'Plan1' AND p.name = 'John' ) )
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
