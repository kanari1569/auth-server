# make gradle zip files
zip -s 0 ./auth-service/gradle/wrapper/gradle-8.1.1.zip --out ./auth-service/gradle/wrapper/gradle-8.1.1-bin.zip

# build stage
cd ./auth-service
./gradlew clean build

# clear temp files 
cd ..
rm ./auth-service/gradle/wrapper/gradle-8.1.1-bin.zip