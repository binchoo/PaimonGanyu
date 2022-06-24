main:

deploy-prod: build
	sam deploy --guided \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

deploy-test: build
	sam deploy --guided \
		--profile serverless \
		--region ap-northeast-1 \
		--parameter-overrides Env=test

build: build-template
	cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :application:copyBuiltZipNoTomcat :application:bootJar

build-template:
	sam build --profile serverless

localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true :application:test
