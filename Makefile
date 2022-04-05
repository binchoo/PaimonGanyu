main: deploy
	cd PaimonGanyu; ./gradlew test
deploy: build
	sam deploy --profile serverless
build:
	cd PaimonGanyu; ./gradlew -x test clean build; ./gradlew buildZip
	sam build --profile serverless
