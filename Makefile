main:

paimonganyu-skill-prod: build
	cd sam/paimonganyu-skill; sam deploy --guided \
		--stack-name paimonganyu-skill \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

paimonganyu-prod: build
	cd sam/paimonganyu; sam deploy --guided \
        --stack-name paimonganyu \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

paimonganyu-test: build
	cd sam/paimonganyu; sam deploy --guided \
        --stack-name paimonganyu-test
		--profile serverless \
		--region ap-northeast-1 \
		--parameter-overrides Env=test

build: build-template
	cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :application:copyBuiltZipNoTomcat :application:copyBootJar

build-template:
	cd sam/paimonganyu; sam build --profile serverless
	cd sam/paimonganyu-skill; sam build --profile serverless

localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true :application:test
