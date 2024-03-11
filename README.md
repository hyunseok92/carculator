# 김현석 세번째 팀 프로젝트 : Car-culator

### [ 이미지 분석 AI 기반 차량관리 플랫폼 ]

![image](https://github.com/doowon13/Car-culator/assets/83566946/bd1fe9d4-3273-490b-81a6-39cd534ae6a3)

<a href="https://www.canva.com/design/DAF3PaeUjOw/7rTUdHqu_TxzK7VJ19XDpw/view?utm_content=DAF3PaeUjOw&utm_campaign=designshare&utm_medium=link&utm_source=editor" style="color:black">
[ 프로젝트 포트폴리오 링크 ]
</a> <-- Click!


## 프로젝트 기능

### 🛡 JWT, OAuth2 소셜로그인 (Kakao, Naver, Google, Git)

> * Kakao, Naver, Google, Git 계정을 통한 간편 로그인이 가능합니다.

### 📊 공공데이터를 활용한 위치기반 정비소 정보제공 기능
> * 2022년 기준 국내 모든 차량 정비소 정보를 제공합니다.
> * 카카오 지도를 이용하여 위치정보를 정확하게 유저에게 제공해줍니다.
> * 지도의 정비소를 선택시 바로 해당 정비소의 예약페이지로 이동, 예약을 진행할 수 있습니다.

<details>
<summary style="cursor: pointer">미리보기</summary>
<div markdown="1">

![image](https://github.com/doowon13/Car-culator/assets/83566946/9bbb9fb9-3227-4aca-a70c-bdcd5749c0f6)
<br>
</div>
</details>

### 👨‍💻 마이페이지 기능 (AI 차량등록, 정보조회, 내 정비소 정보수정, 비밀번호 변경)
> * AI를 활용하여 자신이 등록할 차량의 차종을 직접 입력하지 않아도 이미지 한 장으로 구분해 낼 수 있습니다.
> * 정비소 가입자는 업장의 주소나 전화번호가 바뀌었을 때 수정을 할 수 있습니다.
> * 현재 비밀번호와 같은 비밀번호로 변경이 불가능합니다.
> * 정규식을 통한 영문, 숫자, 특수문자가 포함된 8~16문자를 사용해야 합니다.

<details>
<summary style="cursor: pointer">미리보기</summary>
<div markdown="1" style="text-align:center">

![AI차량등록](https://github.com/doowon13/Car-culator/assets/83566946/6ebd9a64-afb2-4c86-bf29-55c69b176005)
<br>
</div>
</details>

### 🛒 쇼핑몰 기능 (장바구니, 주소검색, 결제시스템)
> * 웹사이트 자체적으로 구현한 차량용품 샵에서 간편하게 결제를 할 수 있습니다.
> * 장바구니 기능을 활용하여 제품을 저장하여 구매할 수 있습니다.
> * 카테고리별, 가격별, 품목명별 정렬을 할 수 있습니다.

<details>
<summary style="cursor: pointer">미리보기</summary>
<div markdown="1">

![KakaoTalk_20240101_123901545](https://github.com/doowon13/Car-culator/assets/83566946/6a7bd773-bbe9-404e-98fc-27dd9b39c6dc)
![KakaoTalk_20240101_124119704](https://github.com/doowon13/Car-culator/assets/83566946/531d2aaf-99ad-4a71-957d-84df37a34232)
![KakaoTalk_20240101_124152869](https://github.com/doowon13/Car-culator/assets/83566946/040ce4c3-48c3-4e26-862e-cbdddece71e3)
<br>
</div>
</details>

### 🇦🇮 AI를 활용한 차량 손상도에 따른 견적제공 기능
> * Ultralytics의 YOLO v8과 AI Hub의 Open DataSet을 사용하여 차량 이미지에서 손상된 부분을 판독하여 견적을 내주는 서비스를 제공합니다.

<details>
<summary style="cursor: pointer">미리보기</summary>
<div markdown="1">

![image](https://github.com/doowon13/Car-culator/assets/83566946/a2c13785-0034-4dc6-8dac-f0a664f8bd30)
<br>
</div>
</details>

### 🚗 차량 정비 예약시스템
> * AI를 활용하여 등록된 정보와 위치정보를 바탕으로 가까운 정비소를 찾아 예약신청을 할 수 있습니다.
> * 누군가 예약을 한 시간에는 다른 유저가 예약할 수 없습니다.
> * 정비소 가입자는 자신의 정비소에 들어온 예약에 대해 승인 또는 반려를 할 수 있습니다.

<details>
<summary style="cursor: pointer">미리보기</summary>
<br>
<div markdown="1">
<strong>일반 유저의 예약</strong>

![image](https://github.com/doowon13/Car-culator/assets/83566946/d6d4b01f-ba75-48db-a7a4-40e2f308090a)
![image](https://github.com/doowon13/Car-culator/assets/83566946/a3ede7ed-4a1a-41b1-9bb2-6b7a6973e18e)

<strong>정비소 유저의 예약관리</strong>
![image](https://github.com/doowon13/Car-culator/assets/83566946/6e26e2b6-455f-452b-b6a7-760951bf0b96)
<br>
</div>
</details>

<br>
<hr style="text-align:center; width:45%;">
<br>

## Back-End 사용기술
`Java` `SpringBoot (MVC model 2)` `JPA` `QueryDSL` `JPQL` `JWT` `Gradle` `Python` `Oracle DB`

## Front-End 사용기술
`Vue.js & Vuetify` `Axios` `TypeScript` `JavaScript` `Node.js`

## AI 사용기술
`Ultralytics` `TensorFlow` `OpenCV` `Pytorch` `Google Colab`

## DB ERD Diagram
![424ae025-f606-4d1e-b6ba-56e50d3f593b](https://github.com/doowon13/Car-culator/assets/83566946/45ccd44b-b743-4058-9771-90339101b85a)
