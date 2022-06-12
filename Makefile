main: deploy
	cd PaimonGanyu; ./gradlew -PlocalTest=true :application:test
deploy: build
	sam deploy --profile serverless
build:
	sam build --profile serverless
	cd PaimonGanyu; ./gradlew -x test clean :application:copyBuiltZipNoTomcat
