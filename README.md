## 플라네타리움

### 조건
~~~
# 문제

클라이언트는 비트코인의 2023년 1월 1일자의 블록 생성 난이도 (`difficulty`)를 예측하고 싶어합니다. 
클라이언트는 시각화 된 자료와 함께 RAW 데이터를 구글 스프레드시트로 보는 것을 즐기기 때문에, 
(1) 현재의 비트코인 블록체인 정보를 먼저 구글 스프레드 시트에 옮기고, (2) 구글 스프레드 시트의 정보를 읽어 블록 생성 난이도 예측에 관한 데이터를 API 로 제공해주어야 합니다. 
클라이언트에서는 해당 정보를 이용하여 블록 생성 난이도와 미래의 블록생성 난이도를 시각화합니다.

당신은 백엔드 엔지니어로서 위에서 언급된 (1) 과 (2) 의 작업을 하여 API 를 제공하는 웹 서버 어플리케이션을 제작해야 합니다.

# 목적

- 백엔드 개발 역량을 고루 평가합니다.
- 평소 어떤 식으로 프로그래밍하는지 그 스타일을 봅니다.
- 다른 프로그래머가 재활용 할 수 있게끔 자신의 코드를 구조화 할 수 있는지를 확인합니다.

# 요구 사항

- [Bitquery](https://graphql.bitquery.io/)를 이용하여 비트코인 블록체인의 정보를 조회해야 합니다.
- 구글 스프레드시트에 블록 정보는, 블록 단위로 실시간 업데이트가 되어야 합니다.
- 블록생성난이도와 예측에 관한 데이터가 제공되는 API 가 있어야 합니다.
    - 해당 웹페이지는 구글 스프레드 시트로부터 정보를 취득해야 합니다.
    - 데이터는 Table 혹은 텍스트 형태로 제공되어야 합니다.
        - 혹은 위의 데이터를 바탕으로 프론트엔드 엔지니어가 웹 서비스를 구현할 수 있도록 json response를 제공하는 것도 가능합니다.

평가 요소

- 중복 제거를 얼마나 고민하는가
- 문제의 부분 문제를 두고, 각 부분 문제의 우선 순위를 잘 설정할 수 있는가
- 주어진 시간 안에 달성할 수 있는가
- 다른 사람이 유지 보수할 수 있게 배려하는가 (문서화, 주석, 네이밍 등)
- 기초적인 알고리즘, 자료 구조 개념
- 문제를 추상화하기 위한 언어적 도구를 잘 활용하는가
- 버전 관리 시스템을 적절하게 사용할 줄 아는가
- 표준적인 코딩 관습을 잘 따르는가
- 코드가 충분히 방어적으로 짜여져 있는가
~~~

## 가정
1. 구글시트에서 api 실시간으로 요청한다.
2. 제네시스블록부터 파싱은 오래 걸려 데이터는 현재블록부터 약 6048(2016 x 3) 전부터 데이터 파싱한다.

## 개발회고록
1. 처음에 요구사항이 잘 이해가 안되었다.
   - https://graphql.bitquery.io 를 이용하여 구글시트를 연동한 뒤 구글시트를 이용하여 데이터를 파싱하라는건지 햇갈렸다.
   - 실시간 업데이트를 위해, websocket통신을 구현해야 하나 고민했다.
2. 요구하는 데이터를 주는 api를 만든 후 구글시트에서 요청 or 배치로 구글시트에 데이터 추가로 가정 하기로 하였다.
3. bitquery와 graphql를 처음 접해봐서 초반에 해맸다.
4. 배치를 통해 bitquery로 블록을 파싱하고 다음 난이도 예측값을 계산후 db에 insert, update 로직을 만들었다.
5. 비트코인 관련 사이트를 서핑해보니, 난이도 예측 관련된 추가 데이터도 있으면 좋겠다는 생각이 들어서 추가 하였다.
6. 채굴된 시간이 UTC기준이라 KST변경 위해 +9시간을 해야하는건지 고민하였지만 UTC기준을 api 명세서에 추가하기로 하였다.
   - ZonedDateTime이 있는데 toLocalDateTime하면 다시 UTC로 준다.

## 후기
1. graphql를 공부해야겠다. spring graphql이 있는데 안정적이지 못하다는 후기가 있다.
2. 구글시트 관련된 api를 찾아봐야겠다. (어디까지 지원이 되는지)
3. 중복되는 네이밍을 효과적으로 사용 할 수 있는 방법을 고민해봐야겠다 (연속적인 BitcoinBlock 사용)


## 개발 환경
- 기본 환경
    - IDE: IntelliJ IDEA
    - OS: Mac
    - GIT
- Server
    - Java11
    - Spring Boot
    - JPA
    - gradle
    - h2

## 빌드. 실행
Intellij , Mac 기준입니다.
- 빌드 ```./gradlew clean build```
- 실행 ```디버그 모드로 실행(control + D)```


## API 명세서

### /api/v1/bitcoin
비트코인 정보조회 api
* **섦명**

  비트코인 list 요청  
  파라미터 height 이상 list 리턴
  - height : bitcoin height
  - blackHash : bitcoin blockhash
  - difficulty : 난이도
  - minde : 채굴된 시간 (UTC기준)
  - transactionCount : 해당 블록 tx 갯수

* **URL**

  /api/v1/bitcoin

* **Method:**

  `GET`

* **URL Params**

  - height (비트코인 height)

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
  ~~~
    [  
        {
            "height": 770000,
            "blockHash": "00000000000000000004ea65f5ffe55bfc0adbc001d3a8e154cc9f19da959ba8",
            "difficulty": 3.536406590045712E13,
            "mined": "2023-01-02T09:27:45",
            "transactionCount": 620
        },
        {
            "height": 770001,
            "blockHash": "0000000000000000000792f6a9d56bdef9b245e63be1e85d52ee07835c48a9d1",
            "difficulty": 3.536406590045712E13,
            "mined": "2023-01-02T09:30:32",
            "transactionCount": 488
        }
   ]
  ~~~

* **Error Response:**

  * **Code:** 500 BAD REQUEST <br />
    **Content:**
  * `{
    "errorCode": 10001,
    "errorMessage": "마지막 채굴된 height 보다 파라미터가 높습니다."
    }`
----

* **섦명**

  비트코인 난이도 예측 조회 api
  - predictionHeight : 난이도 변경 블록 height
  - presentDifficulty : 현재 난이도
  - predictionDifficulty : 난이도 변경 예측 값
  - difficultyChangePercent : 직전 난이도 대비 변경 퍼센트
  - mineAverageTime : 비트코인 평균 채굴 시간 (분)
  - predictionChangeDate : 예상되는 변경 시간 (UTC 기준)
  
* **URL**

  /api/v1/bitcoin/prediction

* **Method:**

  `GET`

* **URL Params**

  - height (비트코인 height)

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
  ~~~
    {
        "predictionHeight": 770112,
        "predictionDifficulty": "34.26T",
        "presentDifficulty": "35.36T",
        "mineAverageTime": 10.32,
        "difficultyChangePercent": -3.12,
        "predictionChangeDate": "2023-01-03T02:06:25"
    }
  ~~~

* **Error Response:**

  * **Code:** 500 BAD REQUEST <br />
    **Content:**
  * `{
    "errorCode": 10003,
    "errorMessage": "예측되는 블록 정보가 없습니다. 관리자에게 문의해주세요."
    }`