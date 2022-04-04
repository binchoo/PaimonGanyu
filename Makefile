main: build_gradle
	sam build --profile serverless
	sam deploy --profile serverless
build_gradle:
	cd PaimonGanyu; ./gradlew -x test build; ./gradlew buildZip
