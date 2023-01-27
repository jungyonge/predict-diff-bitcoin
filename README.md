## 비트코인 난이노 예측

## 개발회고록
1. graphql사용
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

  - NONE

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 
  ~~~
    {
        "predictionHeight": 770112,
        "presentDifficulty": "35.36T",
        "predictionDifficulty": "34.26T",
        "difficultyChangePercent": -3.12,
        "mineAverageTime": 10.32,
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
