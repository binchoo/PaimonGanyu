main:

paimonganyu-test: build-paimonganyu
	cd PaimonGanyu; ./gradlew -Penv=test paimonganyu-app:paimonganyu:sD

paimonganyu-skill-test: build-paimonganyu-skill
	cd PaimonGanyu; ./gradlew -Penv=test paimonganyu-app:paimonganyu-skill:sD

paimonganyu: build-paimonganyu
	cd PaimonGanyu; ./gradlew -Penv=prod paimonganyu-app:paimonganyu:sD

paimonganyu-skill: build-paimonganyu-skill
	cd PaimonGanyu; ./gradlew -Penv=prod paimonganyu-app:paimonganyu-skill:sD

build-paimonganyu:
	cd PaimonGanyu; ./gradlew paimonganyu-app:paimonganyu:sB

build-paimonganyu-skill:
	cd PaimonGanyu; ./gradlew paimonganyu-app:paimonganyu-skill:sB

localtest:
	cd PaimonGanyu; ./gradlew -PlocalTest=true clean test
