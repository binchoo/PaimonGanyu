main:

deploy-prod: build
	sam deploy --guided \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod \
		$(resources)
deploy-test: build
	sam deploy --guided \
		--profile serverless \
		--region ap-northeast-1 \
		--parameter-overrides Env=test \
		$(resources)
build: build-template
	cd PaimonGanyu; ./gradlew -x test clean :application:copyBuiltZipNoTomcat
build-template:
	sam build --profile serverless
localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true :application:test
