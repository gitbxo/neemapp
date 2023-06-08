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
echo '' ; echo ">>> subscription <<<" ; echo ''
curl -X GET http://localhost:8000/rest/subscription/1/1
echo '' ; echo ''
echo '' ; echo ">>> subscription <<<" ; echo ''
curl -X GET http://localhost:8000/rest/subscription/1/2
echo '' ; echo ''
echo '' ; echo ">>> subscription <<<" ; echo ''
curl -X GET http://localhost:8000/rest/subscription/1/3
echo '' ; echo ''


echo ''
curl -H 'Content-Type: application/json' -X PUT \
  -d '{ "id":1, "name":"Plan1", "description":"Plan1", "planStartDate":"2023-01-01", "planEndDate":"2023-12-31", "deductible":301, "overrides": { "ortho":1001, "major":1002 } }' \
  'http://localhost:8000/rest/plan/1'
echo '' ; echo ''


echo ''
curl -H 'Content-Type: application/json' -X PUT \
  -d '{ "patientId":1, "planId":1, "coverageStartDate":"2023-01-01", "coverageEndDate":"2023-12-31", "usedDeductible":202, "usedOverrides": { "ortho":1001, "major":1002 } }' \
  'http://localhost:8000/rest/subscription/1/1'
echo '' ; echo ''

