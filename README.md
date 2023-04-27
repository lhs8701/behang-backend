  

## <img align="left" src="https://user-images.githubusercontent.com/51712973/205058042-9a57f621-f963-49ae-bd32-cb77bf21822b.png" width="80px" /> 비행 - 비와 함께하는 여행
> 비 오는 날 여행할 곳을 추천해주는 어플리케이션 서비스

<img width="996" alt="appstore_screenshot" src="https://user-images.githubusercontent.com/51712973/205062487-2e7b2c1b-d083-4273-b7a3-05378c3e6fdd.png">

[[Web Page]](https://jaeheon-sim.github.io/behang/)   
[[App Store]](https://apps.apple.com/kr/app/%EB%B9%84%ED%96%89-%EB%B9%84%EC%99%80-%ED%95%A8%EA%BB%98%ED%95%98%EB%8A%94-%EC%97%AC%ED%96%89/id1643689621)
## 🌎 프로젝트 배경

자신의 일정에 맞춰 여행 계획을 세우다 보면, 여행 도중 비를 만나는 경우가 종종 있습니다.   
비행은 비가 오더라도 걱정 없이 여행을 즐길 수 있는 서비스가 있으면 좋을 것 같다는 아이디어에서 시작되었습니다. 


## 🌳 프로젝트 개요

- 진행 기간 : 2022.08.01 ~ 2022.09.15


## 🖥 기술 스택
<div align=center> 
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
</div> 


## ⚡ 주요 기능
### 메인 피드
<p align="center">
<img src="https://user-images.githubusercontent.com/51712973/205067198-4a953ce6-7460-409c-86ef-f84f9c07ff37.jpeg" width="200px" />
</p>

- 비올 때 찍은 여행 사진을 피드에 게시할 수 있습니다.   
- 현재 위치로부터 가까운 거리에 있는 관광지순으로 사진이 표시됩니다.   
- 사진을 클릭하면 해당 관광지의 정보와 사진들을 볼 수 있습니다.   

### 포스트 및 업로드
<p align="center">
<img src="https://user-images.githubusercontent.com/51712973/205068684-122f9a3f-6833-489e-be5b-9dff94778c9f.jpeg" width="200px" />
<img src="https://user-images.githubusercontent.com/51712973/205068984-d535f949-08ae-4f16-ac16-32213272258b.jpeg" width="200px" />
</p>

- 게시물의 사진과 이름, 태그 정보를 SNS를 통해 공유할 수 있습니다.    
- 게시물에 등록된 태그 정보를 통해 관광지의 간단한 정보를 한 눈에 확인할 수 있습니다.   
- 피드에서 선택한 사진의 같은 장소 사진을 함께 확인할 수 있습니다.   
- 비 올 때 찍은 여행 사진을 앨범에서 선택하여 게시할 수 있습니다.   
- Tour API를 통해 한국관광공사의 관광지 정보를 위치로 등록할 수 있습니다.   
- 사진을 게시할 때 관광지에 대한 간단한 정보를 태그로 알려줄 수 있습니다.   

### 관광 지도
<p align="center">
<img src="https://user-images.githubusercontent.com/51712973/205070103-b0dc56cc-0295-4028-bf26-5ce995aec9f1.jpeg" width="200px" />
<img src="https://user-images.githubusercontent.com/51712973/205070122-49df6c55-a1c3-40f4-a75d-2685bd8ce831.jpeg" width="200px" />
<img src="https://user-images.githubusercontent.com/51712973/205070806-aeb629de-5235-44c4-963f-9418c10b227c.jpeg" width="200px" />
</p>

- TourAPI의 관광지 정보를 검색할 수 있습니다.   
- 검색한 TourAPI의 관광지 위치를 카카오맵 상에서 확인할 수 있습니다.   
- 내 주변 버튼을 누르면 현재 위치 주변의 관광지 위치를 알 수 있습니다.   

### 여행 기록
<p align="center">
<img src="https://user-images.githubusercontent.com/51712973/205071924-4bd71bf4-c900-417e-aa9b-ca5cfcb8f86a.jpeg" width="200px" />
<img src="https://user-images.githubusercontent.com/51712973/205072106-2fa7bc00-5ba4-4c78-b147-f77e14a75ebf.jpeg" width="200px" />
</p>

- 사용자가 특정 지역에 일정 개수 이상 사진을 게시하면 깃발이 생깁니다.   
- 인스타그램 스토리 연결을 통해 여행 기록을 손쉽게 공유할 수 있습니다.   

### 마이페이지
<p align="center">
<img src="https://user-images.githubusercontent.com/51712973/205072620-6fc4d4c4-754b-4ca6-bd1c-05e1bd36da17.jpeg" width="200px" />
</p>

- 사용자가 게시한 포스트들을 확인할 수 있습니다.
- 프로필 사진, 닉네임을 변경할 수 있습니다.

### 데모 영상

![실행영상_cut_low_](https://user-images.githubusercontent.com/45627010/203982792-ccfbd95d-a691-461e-90d3-30a3c41ac72c.gif)

## 🎮 프로젝트 구조

```

├─java
│  └─bh
│      └─bhback
│          ├─domain
│          │  ├─auth
│          │  │  ├─basic
│          │  │  │  ├─controller
│          │  │  │  ├─dto
│          │  │  │  └─service
│          │  │  ├─jwt
│          │  │  │  ├─dto
│          │  │  │  ├─entity
│          │  │  │  └─repository
│          │  │  └─social
│          │  │      ├─apple
│          │  │      │  ├─controller
│          │  │      │  ├─dto
│          │  │      │  └─service
│          │  │      └─kakao
│          │  │          ├─controller
│          │  │          ├─dto
│          │  │          └─service
│          │  ├─history
│          │  │  ├─controller
│          │  │  ├─dto
│          │  │  ├─entity
│          │  │  └─service
│          │  ├─image
│          │  │  ├─dto
│          │  │  ├─entity
│          │  │  ├─repository
│          │  │  └─service
│          │  ├─place
│          │  │  ├─dto
│          │  │  ├─entity
│          │  │  ├─repository
│          │  │  └─service
│          │  ├─post
│          │  │  ├─controller
│          │  │  ├─dto
│          │  │  ├─entity
│          │  │  ├─repository
│          │  │  └─service
│          │  ├─report
│          │  │  ├─controller
│          │  │  ├─entity
│          │  │  ├─repository
│          │  │  └─service
│          │  ├─test
│          │  └─user
│          │      ├─controller
│          │      ├─dto
│          │      ├─entity
│          │      ├─repository
│          │      └─service
│          └─global
│              ├─common
│              │  ├─jpa
│              │  ├─pagenation
│              │  │  ├─dto
│              │  │  └─service
│              │  ├─request
│              │  └─response
│              │      ├─dto
│              │      └─service
│              ├─config
│              ├─error
│              │  └─advice
│              │      └─exception
│              ├─security
│              └─util
└─resources
    ├─secrets
    └─templates
        └─social
```
