# Santa_Backend
본 코드는 ios, android 어플리케이션 산타의 백엔드 코드입니다. (ver.2.0)

## ✨🎅🏻⛰ about santa

🤟" Enjoy Hiking with SANTA!"
- `산타의 시작`  : ***" 등산, 좀 더 재밌게 즐길 수 있는 방법은 없을까? "*** 라는 물음에서 시작했습니다.
- `산타의 여정` : ***" 마치 게임처럼 지도에 깃발을 꽂고, 다양한 사람들과 경쟁을 할 수 있다면 어떨까? "*** 라는 아이디어를 시작으로 산타가 전국의 등산러들을 하나로 연결해주는 다리가 되어주기로 다짐했습니다.

    그래서 저희는 산타만의 방식으로 등산의 경험을 새롭게 정의했습니다! 게임에 있는 요소들에서 착안하여 **[등산 = 게임 / 산 목록 = 맵 / 산 난이도 = 맵별 난이도 / 랭킹 = 경쟁전]** 이와 같은 컨셉으로 기획하게 되었습니다!
- `**산타의 최종 목적지**` : 모든 등산러들이 더 재밌게 등산을 즐기고 산행의 모든 과정속에서 산타가 동행하는 것입니다!

> 산타 소개 링크:
> https://www.notion.so/makeus-challenge/1b69fde8781146fb904d3331cd84fa0e

## 핵심 기능
> 1. 산 정복하기
: 정복할 산 검색 → 출발 버튼과 함께 등산 시작 → 정상 도착 → 사진 인증 → 깃발 획득 → 정복!!

![정복1](https://user-images.githubusercontent.com/81170119/221339773-852c9938-efa7-4fc0-b05c-330fca16f1c9.png)
![정복2](https://user-images.githubusercontent.com/81170119/221339774-c51d775e-04f5-41f3-8c3d-77b5a38d5010.png)


> 2. 해당 산 일등먹기

![산1등먹기](https://user-images.githubusercontent.com/81170119/221339778-5ce380ab-793b-4f5e-b6a9-eb480e8fd1e9.png)


> 3. 산별, 코스별 난이도 파악하기

![산1등먹기](https://user-images.githubusercontent.com/81170119/221339778-5ce380ab-793b-4f5e-b6a9-eb480e8fd1e9.png)
![산 난이도 파악](https://user-images.githubusercontent.com/81170119/221339779


> 4. 지도에서 깃발 수 늘리기

![지도에서 깃발](https://user-images.githubusercontent.com/81170119/221339776-52ba1c27-812d-4895-8478-eddc3f2c5896.png)


> 5. 배지 모으기

![배지모으기](https://user-images.githubusercontent.com/81170119/221339780-edd9cdb5-80cf-4f4f-928c-86f04ddbc112.png)
-10b8048e-0ce3-4274-be14-d2a5bb13b9fe.png)


## 그 외 기능
- 소셜 로그인: 카카오 로그인, 애플 로그인
- 정복 인증: 산 정복 후 깃발을 꽂으면 인증 사진을 올릴 수 있습니다. 다른 사람들의 정복 사진에 좋아요와 댓글을 남겨 소통도 가능합니다. 
- 게시판: 산 정복을 하지 않아도 다양한 사진들을 올려 등산을 좋아하는 사람들과 소통할 수 있습니다. 
- 푸시 알림: 다른사람이 좋아요를 하거나, 댓글을 남기면 푸시 알림으로 바로 확인할 수 있습니다. 


## Structure 
    > build
    > gradle
    > logs
        | app.log 
        | app-%d{yyyy-MM-dd}.%i.gz
        | error.log 
        | error-%d{yyyy-MM-dd}.%i.gz
    > src.main.java.com.example.demo
        > config
            > secret
                | Secret.java 
            | BaseException.java 
            | BaseResponse.java 
            | BaseResponseStatus.java 
            | Constant.java 
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

<img width="842" alt="무제" src="https://user-images.githubusercontent.com/81170119/221339588-6c2274d4-0983-44a9-be24-a5dda551787b.png">
<img width="768" alt="무제 2" src="https://user-images.githubusercontent.com/81170119/221339592-8b70fe9d-a524-44ac-bccc-b44a628b3baa.png">
<img width="788" alt="무제 3" src="https://user-images.githubusercontent.com/81170119/221339594-486ce1fc-9387-4bd3-a6b7-1d2fb1385815.png">
<img width="776" alt="무제 4" src="https://user-images.githubusercontent.com/81170119/221339595-e0715cc4-91e4-4cdd-b04f-5674b17baef5.png">
<img width="776" alt="무제 5" src="https://user-images.githubusercontent.com/81170119/221339600-5639ccae-01e4-4501-b56c-aa5c46b6f5d0.png">

