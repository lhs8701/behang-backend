
![](readme/img5.png)
# 비행 - 비와 함께 하는 여행 
![](readme/그림3.png)
비행은 비 + 여행의 합성어로, 비올 때 떠나는 여행을 의미있게 만들어주는 서비스입니다.   

[Web Page](https://jaeheon-sim.github.io/behang/)   
[App Store](https://apps.apple.com/kr/app/%EB%B9%84%ED%96%89-%EB%B9%84%EC%99%80-%ED%95%A8%EA%BB%98%ED%95%98%EB%8A%94-%EC%97%AC%ED%96%89/id1643689621)
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
비올 때 찍은 여행 사진을 피드에 게시할 수 있습니다.   
현재 위치로부터 가까운 거리에 있는 관광지순으로 사진이 표시됩니다.   
사진을 클릭하면 해당 관광지의 정보와 사진들을 볼 수 있습니다.   

### 포스트
게시물의 사진과 이름, 태그 정보를 SNS를 통해 공유할 수 있습니다.    
게시물에 등록된 태그 정보를 통해 관광지의 간단한 정보를 한 눈에 확인할 수 있습니다.   
피드에서 선택한 사진의 같은 장소 사진을 함께 확인할 수 있습니다.   

### 게시물 업로드
비 올 때 찍은 여행 사진을 앨범에서 선택하여 게시할 수 있습니다.   
Tour API를 통해 한국관광공사의 관광지 정보를 위치로 등록할 수 있습니다.   
사진을 게시할 때 관광지에 대한 간단한 정보를 태그로 알려줄 수 있습니다.   

### 지도
TourAPI의 관광지 정보를 검색할 수 있습니다.   
검색한 TourAPI의 관광지 위치를 카카오맵 상에서 확인할 수 있습니다.   
내 주변 버튼을 누르면 현재 위치 주변의 관광지 위치를 알 수 있습니다.   

### 여행 기록
사용자가 특정 지역에 일정 개수 이상 사진을 게시하면 깃발이 생깁니다.   
인스타그램 스토리 연결을 통해 여행 기록을 손쉽게 공유할 수 있습니다.   

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