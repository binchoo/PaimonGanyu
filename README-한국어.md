# PaimonGanyu [![](https://img.shields.io/badge/chatbot--06A0CE?logo=kakaotalk&color=FFCD00&logoColor=00000)](https://pf.kakao.com/_mtPFb)

**PaimonGanyu**는 카카오톡 챗봇 프로젝트 [「여행 비서 페이몬!」](https://github.com/binchoo/paimonganyu-doc)의 영어명입니다. 이 챗봇은 게임 [원신](https://genshin.hoyoverse.com/ko/) 유저를 위한 편의성 기능을 제공합니다.

이 저장소는 챗봇 스킬 서버와 백엔드 서버리스 워크플로의 자바 코드 베이스입니다.

## 요구 환경

- JDK 11 이상
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) : `sam deploy` 를 사용하여 PaimonGanyu의 IaC로부터 당신의 AWS 클라우드에 인프라를 배치하므로 필요합니다. 이러한 배포 행위를 알고 싶다면 [AWS SAM specification](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification.html)를 참고하십시오.
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)와 [AWS 계정](https://aws.amazon.com/) : 응용들을 AWS에 배포하려면 당연히 여러분 소유의 AWS 계정이 필요합니다. 또 해당 계정의 크레덴셜을 설정해 둔 CLI 역시 필요합니다.
- AWS SSM에 몇 가지 보안 자원을 배치하기 ([HoyopassPrivateKey 및 HoyopassPublicKey](#applicationsproperties-required) 파라미터를 참고해 주십시오.)
- [Docker CLI](https://docs.docker.com/engine/reference/commandline/cli/)는 로컬에서 시스템 테스트를 수행할 경우에만 필요합니다.

## 두 가지 스택

이 저장소는 두 개의 배포 가능한 [CloudFormation 스택](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/stacks.html)을 정의합니다.

### 1. paimonganyu

챗봇 기능을 지원하는 백엔드 워크플로들과 그 의존성입니다.

**Application pacakge**

`:application:lambda`

**Dependent packages**

`:application:infra`, `:domain`, `:awsutils`, `:hoyoapi`

### 2. paimonganyu-skill

챗봇 유저 요청을 처리하고, 스킬 응답을 렌더링하는 스킬 서버 코드와 그 의존성입니다.

**Application package**

`:application:chatbot`

**Dependent packages**

`:application:infra`, `:domain`, `:ikakao`, `:hoyoapi`

## 프로젝트 일지
[Notion: PaimonGanyu 엔지니어링](https://hollow-leotard-0e1.notion.site/PaimonGanyu-81337fdfe052499f98a2a347f30afbcd) 개인적인 프로젝트 일지입니다.

## paimonganyu


![](https://img.shields.io/badge/lambda--06A0CE?logo=awslambda&color=FF9900&labelColor=FFFFFF) ![](https://img.shields.io/badge/aws%20sam--06A0CE?logo=amazonaws&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/dynamodb--06A0CE?logo=amazondynamodb&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/sqs--06A0CE?logo=amazonsqs&color=FF4F8B&labelColor=FFFFFF) ![](https://img.shields.io/badge/event%20bridge--06A0CE?logo=amazoncloudwatch&color=FF4F8B&labelColor=FFFFFF) ![](https://img.shields.io/badge/s3--06A0CE?logo=amazons3&color=569A31&labelColor=FFFFFF) ![](https://img.shields.io/badge/spring--06A0CE?logo=spring&color=6DB33F&labelColor=FFFFFF)

AWS 서버리스 어플리케이션 모델(AWS SAM)을 채용해 원신 유저를 위한 다양한 유스케이스를 구현합니다. 이슈 [All workflows](https://github.com/binchoo/paimonganyu/issues/1#issuecomment-1087132930) 를 참고하여 아래 나열한 유스케이스를 수행하는 백엔드 워크플로에 대해 알아보십시오.

**유스케이스**

- **호요랩 일일 출석**
  - 신규 유저 일일 출석
  - 크론 기반 자동 매일 출석
- **코드 리뎀션**
  - 신규 유저 코드 리뎀션
  - 신규 리딤 코드 배포
- **통행증 팬아웃**
  - 신규 유저 AWS SNS 퍼블리싱
- DynamoDB 테이블에 **통행증 CRUD 처리**

※ 서버리스 워크플로를 배치하려면, 당신 소유의 AWS 계정이 필요합니다. 

## paimonganyu-skill

![](https://img.shields.io/badge/aws%20sam--06A0CE?logo=amazonaws&color=4053D6&labelColor=FFFFFF&logoColor=4053D6) ![](https://img.shields.io/badge/elastic%20beanstalk--06A0CE?logo=amazonaws&color=FF9900&labelColor=FFFFFF&logoColor=FF9900) ![](https://img.shields.io/badge/springboot--06A0CE?logo=springboot&color=6DB33F&labelColor=FFFFFF) 

챗봇 「여행 비서 페이몬!」(PaimonGanyu) 은 스킬 서버를 거쳐서 호요버스 API와 통신합니다. 스킬 서버는 스프링 부트 어플리케이션입니다. 이 응용을 실행하기 전에 3가지의 `properties` 파일을 미리 설정해야 할 수도 있습니다.
paimonganyu-skill 배포시 Amazon Elastic Beanstalk 응용으로서 CloudFormation 스택 하위에 생성됩니다.

### applications.properties (required)

`:application> src> main> resources> applications.properties`

```properties
amazon.ssm.hoyopass.publickeyname = HoyopassRsaPublicKey
amazon.ssm.hoyopass.privatekeyname = HoyopassRsaPrivateKey
listUserDailyCheck.maxCount = 4
```

- `amazon.ssm.hoyopass.publickeyname`(String)

  당신의 AWS SSM 파라미터 저장소에 생성한 RSA 공개키를 지정합니다. 이 키는 유저 통행증을 암호화에 사용됩니다.

- `amazon.ssm.hoyopass.privatekeyname` (String)

  당신의 AWS SSM 파라미터 저장소에 생성한 RSA 사설키를 지정합니다. 이 키는 방금 전 공개키로 암호화된 통행증을 복호화할 수 있어야 합니다.

- `listUserDailyCheck.maxCount` (Non-negative integer)

  `DailyCheckController` 몇 개의 일일 출석 이력을 보여줄지 설정하는 값입니다.

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

이 값들은 로컬 시스템 테스트들이 사용합니다.

응용이 AWS 프로덕션 환경에 올라가는 때는 IAM 역할과 IAM 정책이 이러한 보안 옵션들을 커버합니다.

### accounts.properties (optional)

`:application> src> test> resources> accounts.properties`

몇 가지 테스트는 기능 점검을 위해 실제 원신 계정을 요구합니다.

계정이 프로퍼티 파일로 제공되지 않을 경우 이러한 테스트들은 실패할 것입니다. 그러나 다수의 테스트 계정을 마련하는 것은 쉬운 일이 아니죠.  그러므로 이 파일은 필수가 아니며, 관련 테스트를 무시하시기 바랍니다.

## Makefile 숏컷

**서버리스 워크플로의 배포**

`make paimonganyu-prod version=1.0.0`

**스킬 서버의 배포**

`make paimonganyu-skill-prod version=1.0.0`

**로컬 시스템 테스트의 수행**(최소 3개의 실제 계정을 사용함)

`make localtest`

## 코드형 인프라(IaC)
- [paimonganyu template](https://github.com/binchoo/PaimonGanyu/blob/master/sam/paimonganyu/template.yaml)
- [paimonganyu-skill template](https://github.com/binchoo/PaimonGanyu/blob/master/sam/paimonganyu-skill/template.yaml)

## 기여하기

적합한 컨트리뷰션은 언제나 환영입니다. 마음껏 질문을 하거나, 이슈를 열거나, 작업을 커밋을 하는 데에 주저하지 마세요.

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
