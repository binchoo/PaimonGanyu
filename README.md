# PaimonGanyu

## Genshin Impact KAKAOTALK Chatbot

### Two Stacks
This repo is a java code base for my custom Genshin Impact applications.
It includes two different stacks.

### paimonganyu-skill
The skill server of my KAKAOTALK chatbot [여행 비서 페이몬](https://github.com/binchoo/paimonganyu-doc).

### paimonganyu
- The behind workflows that support my chatbot.
- Some asynchronous & automatic workflows for Genshin users.
- Webflux web clients, most of which implement synchronous apis, and API adapters to fetch player data from Hoyoverse APIs.

## Engineering Wiki
[Notion: PaimonGanyu 엔지니어링](https://hollow-leotard-0e1.notion.site/PaimonGanyu-81337fdfe052499f98a2a347f30afbcd)

## All workflows

[All workflows](https://github.com/binchoo/paimonganyu/issues/1#issuecomment-1087132930)

There are ways to realize various use cases for Genshin Impact players by leveraging the AWS Serveless Application Model. (AWS SAM)

Use cases:

- Daily Check-In
  - New User Event Daily Check-In
  - Cron-based Automatic Daily Check-In
- Code Redemption
  - New User Event Code Redemption
  - New Redeem Code Distribution
- Hoyopass Fanout
  - New User Hoyopass SNS(AWS SNS) Publishing
- Hoyopass CRUD Operations to DynamoDB Tables

## All java modules

https://github.com/binchoo/PaimonGanyu/issues/7

## Project configurations
1. In order to deploy the serverless workflows, you just need an AWS account. No other configurations required.
2. In order to execute the Springboot skill server or JUnit test classes, three `properties` files **MAY** be configured.

### applications.properties (required)

`:application> src> main> resources> applications.properties`

**Example:**

```properties
amazon.ssm.hoyopass.publickeyname = HoyopassRsaPublicKey
amazon.ssm.hoyopass.privatekeyname = HoyopassRsaPrivateKey
listUserDailyCheck.maxCount = 4
```

- `amazon.ssm.hoyopass.publickeyname`(String)

  Set <u>your</u> AWS SSM Parameter name that is holding a RSA public key that encrypts user's hoyopass credentials.

- `amazon.ssm.hoyopass.privatekeyname` (String)

  Set <u>your</u> AWS SSM Parameter name that is holding the RSA private key that can decrypt the encryption done by the previous public key.

- `listUserDailyCheck.maxCount` (Non-negative integer)

  Configure how many items to be displayed when `DailyCheckController` is requested to show a user history of `UserDailyCheck`.

### amazon.properties (optional)

`:application> src> test> resources> amazon.properties`

**Example:**

```properties
amazon.aws.accesskey=ASDFASDFASDFASDFASDF
amazon.aws.secretkey=asdfasfdfASDFASDFasdfasdfASDFASFd+-*/asdf
amazon.region=ap-northeast-2

amazon.dynamodb.endpoint=https://dynamodb.ap-northeast-2.amazonaws.com
#amazon.dynamodb.endpoint=http://localhost:3306

amazon.ssm.hoyopass.publickeyname = HoyopassRsaPublicKey
amazon.ssm.hoyopass.privatekeyname = HoyopassRsaPrivateKey
```

These properties are only required by the local integration tests. 

The production environment is AWS Lambda, hence IAM roles and IAM policies are responsible to configure these security options.

### accounts.properties (optional)

`:application> src> test> resources> accounts.properties`

Some tests require real Genshin Impact accounts to validate their functionalities. If any user authentication is not provided, those tests fail.

That being said, the preparation step for test accounts is not easy. This properties file is not required. You can give up running the test cases.

## Deployment steps

1. Code, build and test the application/infra/domain modules with Gradle tasks.
2. Use SAM CLI to deploy the PaimonGanyu's artifacts to your AWS account.

### Deploying the serveless workflows (`paimonganyu`)
```bash
cd sam/paimonganyu-skill; sam build --profile serverless
cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :application:copyBuiltZipNoTomcat :application:copyBootJar
cd sam/paimonganyu-skill; sam deploy --guided \
	--stack-name paimonganyu-skill \
	--profile serverless \
	--region ap-northeast-2 \
	--parameter-overrides Env=prod
```

### Deploying the skill server (`paimonganyu-skill`)
```bash
cd sam/paimonganyu-skill; sam build --profile serverless
cd PaimonGanyu; ./gradlew -Pversion=$(version) -x test clean :application:copyBuiltZipNoTomcat :application:copyBootJar
cd sam/paimonganyu; sam deploy --guided \
		--profile serverless \
		--region ap-northeast-2 \
		--parameter-overrides Env=prod
```

### Makefile shortcuts

**Deploying the serveless workflows**

`make paimonganyu-prod version=1.0.0`

**Deploying the skill server**

`make paimonganyu-skill-prod version=1.0.0`

## Gradle tasks

### BuildZipNoTomcat

```groovy
task buildZipNoTomcat(type: Zip) {
    from compileJava
    from processResources
    into('lib') {
        from (configurations.runtimeClasspath) {
            exclude 'tomcat-embed-*'
        }
    }
}
```

To deploy a compiled java handler onto AWS Lambda, the handler class itself and any other dependent runtime classpaths should be packaged in one archive file.

In the sam `template.yaml`, the `CodeUri` property of `AWS::Serverless::Function` resource should point to that file. 

See also, [Deploy Java Lambda functions with .zip or JAR file archives (AWS Docs)](https://docs.aws.amazon.com/lambda/latest/dg/java-package.html#java-package-libraries).

### copyBuiltZipNoTomcat

```groovy
def samBuildDir(String stackName) {
    '../'.repeat(project.depth + 1) + "sam/${stackName}/.aws-sam/build"
}

task copyBuiltZipNoTomcat(type: Copy) {
    def dest = samBuildDir('paimonganyu')
    from buildZipNoTomcat
    into(dest)
    doLast {
        println "$project.name:$name has moved fat-zip into $dest"
    }
}
```

To shorten the value of the `CodeUri` property of a `AWS::Serverless::Function` resource, I copy-paste the generated archive file to the sam build directory.

Below shows the difference of `CodeUri`s, with and without the `copyBuiltZip` task.

| with CopyBuildZip              | without CopyBuildZip                          |
| ------------------------------ | --------------------------------------------- |
| CodeUri: application-1.0.0.zip | CodeUri: .aws-sam/build/application-1.0.0.zip |


### CopyBootJar

```groovy
task copyBootJar(type: Copy) {
    def dest = samBuildDir('paimonganyu-skill')
    from bootJar
    into(dest)
    doLast {
        println "$project.name:$name has moved bootJar into $dest"
    }
}
```
This task is the same as the `CopyBuiltZip`, however it copies the Springboot fat-jar of the skill server.

| with CopyBootJar                          | without CopyBootJar                                 |
|-------------------------------------------|-----------------------------------------------------|
| SourceBundle: paimonganyu-skill-1.0.0.jar | SourceBundle: .aws-sam/build/paimonganyu-skill-1.0.0.jar |

### Running a local system test
Some functionalities need to be verified with real-running infrastructures. eg. A service layer depending on a DynamoDB table.

A docker container can be created from `amazon/dynamodb-local` image, 
and system test classes can use its endpoint url (`http://localhost:3306`) to interact with dynamodb tables.

Running a container, including or excluding some test classes for the system test are managed by the build script of `:application`.

To start a local system test, provide an argument named `-PlocalTest` and set this to be true.
This will run a dynamodb container before the gradle test runs.
```bash
./gradlew -PlocalTest=true :application:test
---
> Task :application:stopRunningDynamoDBContainer
Container stopped: c02efd80497c

> Task :application:startDynamoDBContainer
Container started: fdd3f9b691ef9f026935ef2429d7d067c037b80fe4559225f58fbe12ae6b0394
```

## Help Needed for Quality Assurance

I hope redundant system analysis and test (A&T) activities go with this project.

I always welcome collaborators who can join A&T activities like below:

- Spec Review
- Code Review
- Bug Report/Bug Fix
- Proposal Kick-Off

## LICENSES

**GPLv3**

- [PaimonGanyu](https://github.com/binchoo/PaimonGanyu/blob/master/LICENSE)
- [PaimonGanyu:application](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/application/LICENSE)
- [PaimonGanyu:application:infra](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/application/LICENSE)

**MIT**

- [PaimonGanyu:awsutils](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/awsutils/LICENSE)
- [PaimonGanyu:hoyoapi](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/hoyoapi/LICENSE)
- [PaimonGanyu:ikakao](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/ikakao/LICENSE)
- [PaimonGanyu:domain](https://github.com/binchoo/PaimonGanyu/blob/master/PaimonGanyu/domain/LICENSE)
