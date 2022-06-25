main:

paimonganyu-skill-prod: build
	cd sam/paimonganyu-skill; sam deploy --guided \
		--stack-name paimonganyu-skill \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod Archive='paimonganyu-skill-$(version).jar'

paimonganyu-prod: build
	cd sam/paimonganyu; sam deploy --guided \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

paimonganyu-test: build
	cd sam/paimonganyu; sam deploy --guided \
		--profile serverless \
		--region ap-northeast-1 \
		--parameter-overrides Env=test

build: build-template
	cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :application:copyBuiltZipNoTomcat :application:copyBootJar

build-template:
	cd sam/paimonganyu; sam build --profile serverless
	cd sam/paimonganyu-skill; sam build --profile serverless

beanstalk-bucket:
	cd sam/paimonganyu-skill; sam deploy -t beanstalkbucket.yaml \
		--profile serverless \
		--region ap-northeast-2

localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true :application:test
