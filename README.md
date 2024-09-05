# NotTodo-Aos [다운받으러가기](https://play.google.com/store/apps/details?id=kr.co.nottodo&pcampaignid=web_share)
<img src="https://github.com/user-attachments/assets/69b22791-6333-4688-887f-71287b3f2379" alt="1242_2208" width="1000" height="555" />

## 💟 Contributors

|                                                    [송훈기](https://github.com/SSong-develop)                                                     |                                                 [김준서](https://github.com/giovannijunseokim)                                                  |                                                  [최윤정](https://github.com/cbj0010)                                                  |
|:-------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/51434873?v=4" width="200px" height="200dp">| <img src="https://avatars.githubusercontent.com/u/108331578?v=4" width="200px" height="200dp"> | <img src="https://avatars.githubusercontent.com/u/66460447?v=4" width="200px" height="200dp"> |
|                                            `등록뷰 캘린더`<br> `홈뷰 캘린더`<br> `성취뷰 캘린더`<br>                                           |                                                             <br>`푸시알림`<br>`마이페이지`<br>`낫투두 생성`<br>`낫투두 수정` <br>`소셜로그인` <br>`온보딩`                 |                   <br>`로딩뷰`<br> `홈뷰`<br> `성취 뷰`<br>                             


## 👋 커밋 컨벤션
[Git Convention & Branch Strategy](https://www.notion.so/teamnottodo/Git-Github-Convention-cc880c2e448f4ef8b6555838668cbc03)


## 👋 코드 컨벤션
[Android Coding Convention](https://kotlinlang.org/docs/coding-conventions.html)


## 👋 브랜치전략
**브랜치 유형**
- **main** : 완성된 버전의 코드를 저장하는 브랜치
- **develop** : 개발이 진행되는 동안 완성된 코드를 저장하는 브랜치
- **feature** : 작은 단위의 작업이 진행되는 브랜치
- **hotfix** : 긴급한 오류를 해결하는 브랜치
- **refactor** : 유지보수 및 코드의 수정이 이뤄지는 곳의 브랜치
- 해당 작업을 위한 브랜치를 파서 작업합니다.
- 작업 완료 후 PR을 날리고 팀원들에게 크로스체크 후 머지합니다.

예시)

- dev/feature/이슈번호-작업하는 파일명
- dev/refactor/이슈번호-작업하는 파일명

## 📁 *****Foldering*****
```
📂 app
┣ 📂 manifests
┃ ┣ 📜 AndroidManifest.xml
┣ 📂 kotlin+java
┃ ┣ 📂 org.sopt.Nottodo
┃ ┃ ┣ 📂 data
┃ ┃ ┃ ┣ 📂 local
┃ ┃ ┃ ┣ 📂 remote
┃ ┃ ┃ ┃ ┣ 📂 api
┃ ┃ ┃ ┃ ┣ 📂 intercepter
┃ ┃ ┃ ┃ ┣ 📂 model
┃ ┃ ┃ ┣ 📂 resource
┃ ┃ ┣ 📂 domain
┃ ┃ ┃ ┣ 📂 entity
┃ ┃ ┃ ┣ 📂 repository
┃ ┃ ┃ ┃ ┣ 📂 recommend
┃ ┃ ┃ ┃ ┣ 📂 withdrwal
┃ ┃ ┣ 📂 listener
┃ ┃ ┣ 📂 presentation
┃ ┃ ┃ ┣ 📂 achieve
┃ ┃ ┃ ┣ 📂 addition
┃ ┃ ┃ ┣ 📂 base/fragment
┃ ┃ ┃ ┣ 📂 common
┃ ┃ ┃ ┣ 📂 home
┃ ┃ ┃ ┣ 📂 login
┃ ┃ ┃ ┣ 📂 modification
┃ ┃ ┃ ┣ 📂 mypage
┃ ┃ ┃ ┣ 📂 onboard
┃ ┃ ┃ ┣ 📂 recommend
┃ ┃ ┣ 📂 service
┃ ┃ ┣ 📂 util
┃ ┃ ┃ ┣ 📂 livedata
┃ ┃ ┣ 📂 view
┃ ┃ ┃ ┣ 📂 calendar
┃ ┃ ┃ ┣ 📂 snackbar
┃ ┣ 📂 ui.theme

```

## 📝 프로젝트 설명

---
*낫투두란, 나의 목표를 위해 '하지 않을 일'들을 의미해요.

'낫투두'는 하지 않을 일 즉, 낫투두에 특화된 플래닝 서비스입니다.
서비스 내에서 추천 낫투두 및 실천 방법을 통해
하지 않을 일을 실제로 하지 않도록 온보딩 하고있습니다:)

✔️ Point 1. 오늘 꼭 집중할 낫투두는 3개까지만
지나치게 많은 계획은 오히려 달성을 방해해요. 오늘 하루 꼭 실천할 낫투두만 등록하고, 목표 달성 방해요인을 차단하는 데에 집중해보세요.

✔️ Point 2. 유용한 낫투두를 추천해드려요
낫투두가 낯설더라도 두려워마세요! 생활 속 유용한 낫투두를 미리 준비해두었어요.
어떤 낫투두가 나에게 필요한지 둘러보고 가장 먼저 도전할 낫투두를 선택해보세요.

✔️ Point 3. 한 눈에 확인하는 주간, 월간 달성률
이번주와 이번달 나의 달성률을 캘린더를 통해 편하게 확인할 수 있어요!
달성률에 따라 달라지는 그래픽을 보며, 낫투두 달성을 지속하고 꾸준히 목표에 도전해보세요.

## 📝 문제상황 정의

---
- **NOT TO DO**
    낫투두를 의지만으로는 이뤄내기 참 어려운데요, 내가 세운 낫투두를 실천하기 위한
   구체적인 방법을 의미합니다. 예를 들어, ‘일어나자마자 SNS 하지않기'라는 나와의 약속은
   자기 전 휴대폰을 멀리 떨어트려놓는 것이 낫투두를 지킬 좋은 방법이 될 수 있답니다.
      
      
## 🎯 핵심 타겟

---
😭 투두만으로는 목표 달성이 어려우신가요?
그렇다면, 이젠 하지말아야 할 일을 돌이켜 볼 때!
투두를 지키기 어려운 사람들을 위한 낫투두

## 📍 주요 기능

---

<p align="center">
  <img src="https://github.com/user-attachments/assets/24858f99-31c5-4a11-b0da-0902a26e3874" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/b7c5b076-4fe9-4a10-8f53-3ed4fd382fa0" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/98570faf-3e38-4d46-b85d-5ae5ba43dc50" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/80f86062-f18f-4dd4-82c6-0b99ae6a9f56" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/611b45a8-fe06-48e6-9519-360cd37e79b6" align="center" width="30%">
  <img src="https://github.com/user-attachments/assets/f28aaefe-20f3-41f9-b9b6-4a1685576d3b" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/298240ef-c927-408e-8821-6488265252ec" align="center" width="30%">  
  <img src="https://github.com/user-attachments/assets/9c892cf5-f760-4df9-a128-5ec9ae72607c" align="center" width="30%">  
</p>





