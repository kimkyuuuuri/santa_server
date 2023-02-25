# Santa_Backend
본 코드는 ios, android 어플리케이션 산타의 백엔드 코드입니다. (ver.2.0)

## ✨🎅🏻⛰ about santa

🤟" Enjoy Hiking with SANTA!"
`산타의 시작`  : ***" 등산, 좀 더 재밌게 즐길 수 있는 방법은 없을까? "*** 라는 물음에서 시작했습니다.
`산타의 여정` : ***" 마치 게임처럼 지도에 깃발을 꽂고, 다양한 사람들과 경쟁을 할 수 있다면 어떨까? "*** 라는 아이디어를 시작으로 산타가 전국의 등산러들을 하나로 연결해주는 다리가 되어주기로 다짐했습니다.
그래서 저희는 산타만의 방식으로 등산의 경험을 새롭게 정의했습니다! 게임에 있는 요소들에서 착안하여 **[등산 = 게임 / 산 목록 = 맵 / 산 난이도 = 맵별 난이도 / 랭킹 = 경쟁전]** 이와 같은 컨셉으로 기획하게 되었습니다!
`**산타의 최종 목적지**` : 모든 등산러들이 더 재밌게 등산을 즐기고 산행의 모든 과정속에서 산타가 동행하는 것입니다!

## 핵심 기능
1. 산 정복하기
: 정복할 산 검색 → 출발 버튼과 함께 등산 시작 → 정상 도착 → 사진 인증 → 깃발 획득 → 정복!!
![6.5형-2.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7f5cb48a-b0c2-4caa-a1e5-93eecfc5a459/6.5형-2.png)
![6.5형-3.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/886e2b14-9c32-457b-9ed3-0c319afd9681/6.5형-3.png)

2. 해당 산 일등먹기
![6.5형-4.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/dee93c1e-a726-472a-b2d9-a64ade2ed7a5/6.5형-4.png)

3. 산별, 코스별 난이도 파악하기
![6.5형-6.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/62cdbda3-ac7d-4a3d-94b4-2a4b43d1f529/6.5형-6.png)

4. 지도에서 깃발 수 늘리기
![6.5형-5.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6e7b9f6c-061e-4cf0-bb02-66b2e58d1c87/6.5형-5.png)

5. 배지 모으기
![6.5형-7.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/20b74edf-68e3-463e-a560-02e3bcc79f73/6.5형-7.png)

산타 소개 링크: 
https://www.notion.so/makeus-challenge/1b69fde8781146fb904d3331cd84fa0e

## 그 외 기능
소셜 로그인 - 카카오 로그인, 애플 로그인
정복 인증 - 산 정복 후 깃발을 꽂으면 인증 사진을 올릴 수 있습니다. 다른 사람들의 정복 사진에 좋아요와 댓글을 남겨 소통도 가능합니다. 
게시판 - 산 정복을 하지 않아도 다양한 사진들을 올려 등산을 좋아하는 사람들과 소통할 수 있습니다. 
푸시 알림 - 다른사람이 좋아요를 하거나, 댓글을 남기면 푸시 알림으로 바로 확인할 수 있습니다. 


## Structure 
build
    > gradle
logs
    | app.log 
    | app-%d{yyyy-MM-dd}.%i.gz
    | error.log 
    | error-%d{yyyy-MM-dd}.%i.gz
src.main.java.com.example.demo
    > config
        > secret
            | Secret.java 
        | BaseException.java 
        | BaseResponse.java 
        | BaseResponseStatus.java 
        | Constant.java /
    > src
        > domain 별
            > models // 네이밍 규칙: 메소드 (ex. Get, Post) + 도메인 (User) + 요청 방식(Res,Req)
                | GetOOORes.java
                | PostOOOReq.java
                | PostOOORes.java
            | OOOController.java
            | OOOProvider.java
            | OOOService.java
            | OOODao.java
    > utils // 소셜 로그인에 필요한 key, jwt, 정규식, 서버 시작 클래스 등
    > resources
        | application.properties // database, s3, 포트 지정 등 설정 파일 
        | logback-spring.xml
    build.gradle 
    .gitignore

## api 명세서
