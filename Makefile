main: deploy
	cd PaimonGanyu; ./gradlew test
deploy: build
	sam deploy --profile serverless
build:
	sam build --profile serverless
	cd PaimonGanyu; ./gradlew -x test clean :application:copyBuiltZip
