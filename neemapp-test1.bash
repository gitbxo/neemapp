#!/bin/bash


echo ''
echo 'Make sure to run the application using this command:'
echo '    mvn clean spring-boot:run'
echo ''


for item in user patient plan subscription
  do echo '' ; echo ">>> $item <<<" ; echo ''
     curl -X GET http://localhost:8000/rest/$item/1
     echo '' ; echo ''
done

echo ''
curl -H 'Content-Type: application/json' -X PUT \
  -d '{ "id":1, "name":"Plan1", "description":"Plan1", "deductible":300, "overrides": { "ortho":1001, "major":1002 } }' \
  'http://localhost:8000/rest/plan/1'
echo '' ; echo ''

