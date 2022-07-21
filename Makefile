main:

paimonganyu-skill-prod: build-paimonganyu-skill
	cd sam/paimonganyu-skill; sam deploy --guided \
		--stack-name paimonganyu-skill \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

paimonganyu-prod: build-paimonganyu
	cd sam/paimonganyu; sam deploy --guided \
        --stack-name paimonganyu \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod

paimonganyu-test: build-paimonganyu
	cd sam/paimonganyu; sam deploy --guided \
        --stack-name paimonganyu-test
		--profile serverless \
		--region ap-northeast-1 \
		--parameter-overrides Env=test

build-paimonganyu:
	cd sam/paimonganyu; sam build --profile serverless
	cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :paimonganyu-app:paimonganyu:copyZipNoTomcat

build-paimonganyu-skill:
	cd sam/paimonganyu-skill; sam build --profile serverless
	cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :paimonganyu-app:paimonganyu-skill:copyBootJar

localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true clean test
