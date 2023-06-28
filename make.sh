cd msvc-auth-server-sprgbt/
mvn clean && mvn package -DskipTests
docker build . -t auth-server
cd ..

cd msvc-eureka-server-sprgbt/
mvn clean && mvn package -DskipTests
docker build . -t eureka-server
cd ..

cd msvc-gateway-api-sprgbt/
mvn clean && mvn package -DskipTests
docker build . -t gateway-api
cd ..

cd msvc.api.account/
mvn clean && mvn package -DskipTests
docker build . -t account-service
cd ..

cd msvc.api.users/
mvn clean && mvn package -DskipTests
docker build . -t users-service
cd ..

docker-compose up