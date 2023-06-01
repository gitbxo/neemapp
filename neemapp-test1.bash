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
