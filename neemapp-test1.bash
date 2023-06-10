#!/bin/bash


echo ''
echo 'Make sure to run the application using this command:'
echo '    mvn clean spring-boot:run'
echo ''


for item in user patient plan
  do echo '' ; echo ">>> $item <<<" ; echo ''
     curl -X GET http://localhost:8000/rest/$item/1
     echo '' ; echo ''
done

for patient in `psql -U neemapp -c 'SELECT patient_id , plan_id FROM subscription' | awk '-F|' '/\|/ && !/patient/{print $1}' | sort -u`
  do echo '' ; echo ">>> patient = $patient <<<" ; echo ''
     curl -X GET http://localhost:8000/rest/patient/$patient
     echo '' ; echo ''
done


for plan in `psql -U neemapp -c 'SELECT patient_id , plan_id FROM subscription' | awk '-F|' '/\|/ && !/patient/{print $2}' | sort -u`
  do echo '' ; echo ">>> plan = $plan <<<" ; echo ''
     curl -X GET http://localhost:8000/rest/plan/$plan
     echo '' ; echo ''
done


echo '' ; echo ">>> create plan <<<" ; echo ''
curl -H 'Content-Type: application/json' -X POST \
    -d '{ "name":"Plan4", "description":"Plan4", "planStartDate":"2023-02-01", "planEndDate":"2023-10-31", "deductible":401, "overrides": { "ortho":2001, "major":2002 } }' \
    'http://localhost:8000/rest/plan'
echo '' ; echo ''


for patient in `psql -U neemapp -c 'SELECT id , name FROM patient' | awk '-F|' '/John/ {print $1}' | head -1`
  do for plan in `psql -U neemapp -c 'SELECT patient_id , plan_id FROM subscription' | awk '-F|' "/$patient/"'{print $2}' | sort -u`
    do echo '' ; echo ">>> subscription <<<" ; echo ''
    curl -X GET http://localhost:8000/rest/subscription/$patient/$plan
    echo '' ; echo ''
  done

  for plan in `psql -U neemapp -c 'SELECT id , name FROM insurance_plan' | awk '-F|' '/Plan3/ {print $1}' | head -1`
    do echo '' ; echo ">>> update plan <<<" ; echo ''
    curl -H 'Content-Type: application/json' -X PUT \
      -d '{ "name":"Plan3", "description":"Plan3", "planStartDate":"2023-01-01", "planEndDate":"2023-12-31", "deductible":301, "overrides": { "ortho":1001, "major":1002 } }' \
      'http://localhost:8000/rest/plan/'$plan
    echo '' ; echo ''

    echo '>>> update subscription <<<' ; echo ''
    curl -H 'Content-Type: application/json' -X PUT \
      -d '{ "coverageStartDate":"2023-01-01", "coverageEndDate":"2023-12-31", "usedDeductible":202, "usedOverrides": { "ortho":1001, "major":1002 } }' \
      'http://localhost:8000/rest/subscription/'$patient/$plan
    echo '' ; echo ''

  done

  for plan in `psql -U neemapp -c 'SELECT id , name FROM insurance_plan' | awk '-F|' '/Plan4/ {print $1}' | head -1`
    do echo '' ; echo ">>> update plan <<<" ; echo ''
    curl -H 'Content-Type: application/json' -X PUT \
      -d '{ "name":"Plan4", "description":"Plan4", "planStartDate":"2023-06-01", "planEndDate":"2023-10-31", "deductible":501, "overrides": { "ortho":3001, "major":3002 } }' \
      'http://localhost:8000/rest/plan/'$plan
    echo '' ; echo ''

    echo '>>> create subscription <<<' ; echo ''
    curl -H 'Content-Type: application/json' -X POST \
      -d '{ "coverageStartDate":"2023-07-01", "coverageEndDate":"2023-10-31", "usedDeductible":202, "usedOverrides": { "ortho":1001, "major":1002 } }' \
      'http://localhost:8000/rest/subscription/'$patient/$plan
    echo '' ; echo ''

  done
done

