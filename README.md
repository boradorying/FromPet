# 반려동물 커뮤니티 앱 FromPet

![frompet](https://github.com/NBCampFinalProject/FromPet/assets/106301222/7107c904-d779-4371-a31a-7582e0334cae)

- 배포 : 구글 플레이 스토어 검색 FROMPET
- Test ID : test@test.com
- Test PW : 123123


<br>



<div align="center">
<h2>[2023] 반려동물 커뮤니티 From Pet 어플 제작 🐕</h2>
사용자는 반려동물의 정보를 입력하여 회원가입을 진행하며 자신과 가까운 사용자를 조회하며 채팅 & 커뮤니티 & 지도를 이용할 수 있는 어플입니다~! <br> 사람 대 사람으로 이루어지는 어플이기 때문에 개발자가 사용자 입장에서 많은 이용을 해보며 런칭하게 되었습니다. <br> 여러 반려동물의 정보와 상대방과 채팅을 하면서 친해져봐요 🙌
</div>

<br/>


## 목차
  - [팀원 소개](#팀원-구성)
  - [개발환경](#1-개발환경)
  - [프로젝트 소개](#2-프로젝트-소개)
  - [페이지별 기능](#3-페이지별-기능)
  - [이용 방법](#4-이용-방법)
  - [개선 목표](#5-개선-목표)
  - [프로젝트 후기](#6-프로젝트-후기)
 

<br/>


## 팀원 구성

<div align="center">

| **박세준(팀장)** | **사석현(부팀장)** | **이승현(팀원)** | **이수진(팀원)** |
| :------: |  :------: | :------: | :------: |
| [<img src= "https://github.com/NBCampFinalProject/FromPet/assets/106301222/3a256a71-5a3f-4fa2-8e69-81086de97bd8" height=180 width=180> <br/> @boradorying](https://github.com/boradorying) | [<img src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/9c8d5ad1-4a24-4ea4-b374-c8c9161fa6f3" height=180 width=180> <br/> @4seokhyeon](https://github.com/4seokhyeon)| [<img src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/11049fcc-d48f-4cfa-a839-f85f1d3f12fc" height=180 width=180> <br/> @shyr0809](https://github.com/shyr0809)|[<img src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/3826a521-c98c-4f73-b944-25c704bd87b1" height=180 width=180> <br/> @sooj36](https://github.com/sooj36) |
</div>

## 1. 개발환경 
 - 안드로이드 스튜디오
 - 데이터 베이스 및 서버: FireBase
 - 데일리 스크럼 및 트러블 슈팅 : [Notion](https://www.notion.so/12-b5e63529398b49e68ab10b59e636e6ea)
 - 디자인 : [Figma](https://www.figma.com/file/78mamyOeOlT9VOeaRywoH7/12%EC%A1%B0-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?type=design&node-id=59-12&mode=design&t=l6CIi1VZS6vIykLs-0)
 - 코드 컨벤션 : [Notion](https://www.notion.so/618057cdc78149119e50a43eebb110d6)
<br/>

 - 프로젝트 지속기간: 2023.10 -2023.11.14

 - 개발 엔진 및 언어: Android Studio & Kotlin
 

<br/>


## 2. 프로젝트 소개
- FromPet은 반려동물과 함께 소중한 시간을 보내고 있는 사람들을 타겟팅한 반려동물 커뮤니티 어플입니다.
- 귀여운 반려 동물을 카드에서 발견하면 상대방에게 좋아요를 보냄으로써 더욱 깊은 이야기를 할 수 있게 채팅이 구현되어있습니다!
- 또한 내가 보고싶은 반려동물의 종류를 필터링을 통해 유저에게 필터링을 제공합니다.
- 1인 가구 ↑ -> 반려동물 수요 ↑ -> 반려동물 종류 多에 대해서 타겟을하여 반려동물과 같이 사는 사용자를 타겟 하였습니다.


## 3. 페이지별 기능

### [초기화면]
* * *
- 첫 화면은 splash 화면이 뷰페이저로 나타납니다.
    - 로그인이 되어 있지 않은 경우 : Login 화면
    - 로그인이 되어 있는 경우 : Home 화면
    - 소셜 로그인(구글로그인), 이메일 기반 회원가입 및 로그인
 

<div align="left">

<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/aac2a14e-4de0-41cf-99a6-b863652eb26c.gif"/> 
</div>

### [회원가입]
* * *
- 이메일 형식으로 회원가입을 진행 할 수 있으며 이메일 형식 & 비밀번호 6자리 이상으로 가입이 가능합니다.
- 또한 소셜 로그인 기능도 추가해놓았기 때문에 구글 로그인 버튼을 누르면 빠르게 회원가입&로그인이 가능합니다.

  

<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/cfae4ae3-a6c8-42ce-919b-d40c86db1361.gif"/>
<br>


### [프로필 설정]
* * *
- 첫 회원가입을 하게 되면 자동으로 회원정보를 입력하는 창이 뜨게 됩니다.
- 이때 회원 정보를 저장하지 않거나 넘어갈 수 없게 하여 예외가 생길 수도 있는 부분을 배제 하였습니다.
- 작성이 완료되면 자동적으로 홈화면으로 넘어갑니다.

 <img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/9b47174b-0b71-434e-a465-decc35e99cba.gif"/> 
<br>

### [로그인]
* * *
- 로그인은 소셜 로그인와 이메일 로그인 둘을 지원합니다.
- 로그인을 하면 회원 정보 여부에 따라서 화면이 전환됩니다.
- 회원 정보가 없을 경우 : 회원정보 입력 화면
- 회원 정보가 있을 경우 : 홈 화면
<div align="center">
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/86e81ee8-55c7-42ae-b242-b0e645717624.gif"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/b360d26a-f31c-416d-8489-feccb93a4b24.gif"/>
</div>

### [로그아웃]
* * *
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/65918bae-5399-4668-af2a-c4a2cc13dc5f.git"/>
<br>

### [홈 화면]
* * *   
- 스와이프 하여 어플 사용자에게 좋아요 표시를 해보세요!
- 화살표를 누르면 상대방의 프로필을 자세하게 볼 수 있습니다.
- 필터를 사용해 내가 원하는 반려동물을 필터링 해서 조회 해볼 수 있습니다.
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/33536db5-2d6f-4ef2-9a5a-3d44f7990e54"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/bd25f7f0-b087-440f-b24d-a2c926c746af"/>
<br>

### [채팅 화면]
* * *
- 채팅화면은 홈화면에서 매칭이 서로 된 유저끼리만 채팅을 지원합니다.
- 오른쪽 목록은 나에게 좋아요를 보낸 유저의 프로필을 조회할 수 있습니다.
- 실시간으로 상대방과 채팅을 할 수 있습니다.
- 물론 상대방이 채팅에서 마음에 들지 않는다면 우측 상단의 나가기 버튼으로 채팅방을 닫을 수 있습니다.
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/0611251d-df5d-4a9c-b8e5-287ddfc577a5"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/038c1efe-e241-496d-a818-596fe26a037b"/>
<br>

### [지도 화면]
* * *
- 내 위치를 기반해서 근처의 유저가 있는지 지도상으로 찾아볼 수 있습니다.
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/bbcfb994-0a6f-43da-a222-c3ef604fe6f6"/>
<br>

### [커뮤니티]
* * *
- 커뮤니티를 통해 다른 유저들과 여러가지 정보를 공유할 수 있습니다.
- 각 동물 이미지에 맞는 정보들만 카테고리로 조회할 수 있습니다.
- 클립으로 원하는 정보들만 모아서 볼 수 있고 상단에 검색을 통해 내가 원하는 키워드만 검색할 수 있습니다.
- 커뮤니티에서 원하는 정보에 댓글과 대댓글을 달아 상대방과 소통을 할 수 있습니다.
- 커뮤니티 댓글에서 10번 신고가 되면 댓글이 삭제됩니다.
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/97335949-4714-48c5-9fca-bae1b3b94706"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/3bca7a63-6b6f-4d01-b4c5-0773b99ba546"/>
<br>

### [마이페이지 화면]
* * *
- 마이페이지에서 프로필 수정을 할 수 있습니다.
- 내가 어떤 사용자와 친구가 되어있는지도 한번에 조회 가능합니다.
- 알림관리에서 토글 버튼으로 알림기능을 on&off 할 수 있습니다.
- 설정에서는 회원 탈퇴와 비밀번호 초기화를 진행 할 수 있습니다.
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/7e6821ce-eaa6-45c9-b9b5-01adc47f180d"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/903b299c-7269-40ba-9463-dd175d7ac1cb"/>
<br/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/73b11397-2d1b-48da-839f-4f66cc1f4020"/>
<img width="30%" src="https://github.com/NBCampFinalProject/FromPet/assets/106301222/3cf92a25-dc17-4c9c-a7bf-da82290b735c)"/>


## 4. 이용 방법
- 홈화면에서 마음에 드는 반려동물을 좋아요 해보세요! <br/>
그럼 상대방에게 좋아요 표시가 됩니다! 하지만 너무 연연하지 말아요 상대방이 마음에 들지 않으면 거절 할 수도 있답니다...🫢 좌절은 No~ No~

-  반려 동물 커뮤니티 어플입니다. <br/>
  홈화면에서 유저를 카드뷰로 확인할 수 있으며 위의 화면과 같이 자신이 원하는 상대방의 대한 정보를 필터링하여 식별 할 수 있습니다.🙏<br/>
- 상대방과 채팅을 하며 친해져 보세요! <br/>
  매칭된 상대방과 여러정보를 공유하며 채팅으로써 친해져보세요! 물론 상대방이 맘에 들지 않으면 나가기로 친구를 아예 끊을 수도 있습니다! 🫨<br/>
- FromPet만의 커뮤니티를 이용해 보세요 ! 💬<br/>
  상단에는 매칭된 유저의 등수와 쌍을 확인할수가 있고 하단에는 반려동물의 타입에 따라서 카테고리 정보를 각각 조회해 볼 수 있습니다! 🌈 <br/> 사용자의 반려동물 타입 요구가 많아지면 추후에 업데이트를 할 예정입니다!🤔


## 5. 개선 목표
- 배포이후에 사용자의 니즈를 수렴하여 꾸준한 업데이트를 할 예정 [Notion](https://www.notion.so/02fe3fcc274c427faebca42a290a2d90)
- MVVM 패턴이 적용되지 않은 부분을 리펙토링하여 유지보수
    - 로그인 로직에 사용된 Dagger Hilt에 대한 팀원 전체 이해도 증가 -> 모든 구조에 적용(리펙토링)
    - 파이어베이스 서버를 AWS 혹은 데이터 베이스 수정으로 빠릿한 어플, 최적화에 유지보수를 할 예정
 
## 6. 프로젝트 후기  

### 박세준
원했던 기능은 다 완성했지만 클린코드 ,패턴을 제대로 적용하지 못해서 회의감이 많이 들었습니다.
아직도 너무너무 부족한 것이 많다고 느끼고 끝나더라도 꾸준히 공부하면서 했던 프로젝트 코드리팩토링하면서 공부할 것 같습니다!그리고 6주동안 고생하신 팀원분들께 너무 감사드립니다~!
<br>

### 사석현
마지막 프로젝트를 진행하면서 주제를 처음에 정하고 배포까지의 경험이 짧은 시간일 수도 있겠지만 한달을 밀도 있고 계획을 세워가며 팀 프로젝트를 진행하면서 소통과 협업에 대해서 많이 배웠던것 같습니다.
프로젝트 구조의 체계적인 설계의 중요성을 느낄수 있었고 효율적인 프로젝트를 진행할 수 있도록 발전하고 싶습니다. 모두 고생하셨습니다!
<br>

### 이승현
이번 프로젝트를 통해 팀원들과 함께 성장하고, 강한 팀워크와 신뢰를 구축하였습니다. 새로운 기술을 배우고 문제를 해결하며, 각자의 전문성을 향상시켰습니다.
몇몇 부분에서는 더 나은 결정을 내릴 수 있었을 것이라는 깨달음을 얻었습니다. 이 프로젝트가 끝나더라도 우리의 팀워크는 계속될 것이며, 이 경험은 앞으로도 좋은 일만 가득한 기회가 될 것입니다. 감사합니다.
<br>

### 이수진
그동안 했던 과제들을 단순 응용하는 게 아닌 처음 해보는 기능의 연속이라 힘든 순간들이 있었지만, 
함께하며 문제를 해결하는 각자의 생각을 듣게 되어 많이 배우는 시간이었습니다
단순 기능 구현이 아닌 어떻게 구조를 짜고 설계를 하는지 또 그게 왜 중요한지를 많이 느끼게 된 시간이었습니다.
<br>

