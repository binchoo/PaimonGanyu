# PaimonGanyu [![](https://img.shields.io/badge/chatbot--06A0CE?logo=kakaotalk&color=FFCD00&logoColor=00000)](https://pf.kakao.com/_mtPFb)

**PaimonGanyu** is the English name for  [「여행 비서 페이몬!」](https://github.com/binchoo/paimonganyu-doc), which is a Kakaotalk chatbot that provides convenience features for [Genshin Impact](https://genshin.hoyoverse.com/en/) users.

This repository is the java code base for the chatbot's skill-server & back-end serverless workflows.

## Requirements

- JDK 11 or later
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) : You require the SAM CLI to run `sam deploy` to construct your own AWS infrastructure from the IaC of **PaimonGanyu.** See also, [AWS SAM specification](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification.html).
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html) and your [AWS account](https://aws.amazon.com/) : To deploy the applications, you obviously need your own AWS account and the CLI that is holding the account credentials.
- Serveral security resources in AWS SSM. (See, [HoyopassPrivateKey and HoyopassPublicKey](#applicationsproperties-required) parameters)
- [Docker CLI](https://docs.docker.com/engine/reference/commandline/cli/) for running local system tests only if you want to.

## Two stacks

This repository contains two [CloudFormation stacks](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/stacks.html) that AWS SAM deploys to your AWS cloud.

### 1. paimonganyu

The behind AWS workflows that support the chatbot's features, and dependencies.

**Application module**

`:paimonganyu-app:paimonganyu`

**Dependent modules**

`:paimonganyu-infra`, `:paimonganyu-domain`, `:paimonganyu-hoyoapi`, `:awsutils`

### 2. paimonganyu-skill

The code of skill server that handles user requests and renders skill responses, and dependencies.

**Application module**

`:paimonganyu-app:paimonganyu-skill`

**Dependent modules**

`:paimonganyu-infra`, `:paimonganyu-domain`, `:paimonganyu-hoyoapi`, `:ikakao` 

## Engineering Wiki
[Notion: PaimonGanyu 엔지니어링](https://hollow-leotard-0e1.notion.site/PaimonGanyu-81337fdfe052499f98a2a347f30afbcd)

## paimonganyu


![](https://img.shields.io/badge/lambda--06A0CE?logo=awslambda&color=FF9900&labelColor=FFFFFF) ![](https://img.shields.io/badge/aws%20sam--06A0CE?logo=amazonaws&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/dynamodb--06A0CE?logo=amazondynamodb&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/sqs--06A0CE?logo=amazonsqs&color=FF4F8B&labelColor=FFFFFF) ![](https://img.shields.io/badge/event%20bridge--06A0CE?logo=amazoncloudwatch&color=FF4F8B&labelColor=FFFFFF) ![](https://img.shields.io/badge/s3--06A0CE?logo=amazons3&color=569A31&labelColor=FFFFFF) ![](https://img.shields.io/badge/spring--06A0CE?logo=spring&color=6DB33F&labelColor=FFFFFF)

There are ways to realize various use cases for Genshin Impact players by leveraging the AWS Serveless Application Model(AWS SAM). Please refer to the issue [All workflows](https://github.com/binchoo/paimonganyu/issues/1#issuecomment-1087132930) to get more information about the back-end workflows that fulfill the use cases below.

**Use cases**

- **Daily Check-In**
  - New User Event Daily Check-In
  - Cron-based Automatic Daily Check-In
- **Code Redemption**
  - New User Event Code Redemption
  - New Redeem Code Distribution
- **Hoyopass Fanout**
  - New User Hoyopass SNS(AWS SNS) Publishing
- **Hoyopass CRUD Operations** to DynamoDB Tables

※ In order to deploy the serverless workflows, you should have your own AWS account. 

## paimonganyu-skill

![](https://img.shields.io/badge/aws%20sam--06A0CE?logo=amazonaws&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/elastic%20beanstalk--06A0CE?logo=amazonaws&color=FF9900&labelColor=FFFFFF&logoColor=FF9900) ![](https://img.shields.io/badge/springboot--06A0CE?logo=springboot&color=6DB33F&labelColor=FFFFFF) 

The chatbot 「여행 비서 페이몬!」(PaimonGanyu) communicates with the Hoyovers APIs via the skill server, which is a Spring Boot application. This may require you to prepare three `properties` before running the application.
paimonganyu-skill will be deployed to your AWS cloud as an application of Amazon Elastic Beanstalk under a CloudFormation stack.

### applications.properties (required)

`:application> src> main> resources> applications.properties`

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

```properties
amazon.aws.accesskey=ASDFASDFASDFASDFASDF
amazon.aws.secretkey=asdfasfdfASDFASDFasdfasdfASDFASFd+-*/asdf
amazon.region=ap-northeast-2

amazon.dynamodb.endpoint=https://dynamodb.ap-northeast-2.amazonaws.com
#amazon.dynamodb.endpoint=http://localhost:3306

amazon.ssm.hoyopass.publickeyname = HoyopassRsaPublicKey
amazon.ssm.hoyopass.privatekeyname = HoyopassRsaPrivateKey
```

These are only required by the local integration tests. 

The production environment is AWS, hence IAM roles and IAM policies are responsible to configure security options.

### accounts.properties (optional)

`:application> src> test> resources> accounts.properties`

Some tests need your real Genshin Impact accounts to validate their functionalities. 

If any account authentication is not provided, those tests will fail. That being said I know account preparation of your own is not easy. This properties file is not required. You can give up running test cases.

## Makefile shortcuts

**Deploy the serveless workflows**

`make paimonganyu-prod version=1.0.0`

**Deploy the skill server**

`make paimonganyu-skill-prod version=1.0.0`

**Run a local system test**

`make localtest`

## IaC
- [paimonganyu template](https://github.com/binchoo/PaimonGanyu/blob/master/sam/paimonganyu/template.yaml)
- [paimonganyu-skill template](https://github.com/binchoo/PaimonGanyu/blob/master/sam/paimonganyu-skill/template.yaml)

## Contribution

Your gentle contributions are always welcome. Please feel free to ask questions, to open issues, and to commit your works.

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
