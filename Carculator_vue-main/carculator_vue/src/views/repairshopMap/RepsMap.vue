// webpackChunkName: "reps-map"
<script setup>
import axios from 'axios';
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '내 주변 정비소',
    disabled: false,
  },
]
</script>

<template>
  <VCard>
    <VRow>
      <VCol cols="12">
        <VBreadcrumbs :items="breadcrumbs" divider="-" class="px-0" />
      </VCol>
      <VCol cols="6">
        <div class="kakaoMap" style="border: 1px solid brown; width: 100%; height: 400px;" ref="kakaoMap"></div>
      </VCol>
      <VCol cols="6">
        <div class="repList" style="width: 100%; height: 400px; overflow: auto;">
          <VList>
            <VListItem v-for="(item, index) in filteredData" :key="index" @click="goToDetail(item.repairshop_code)">
              <VListItemText>
                <VListItemTitle class="font-weight-bold">상호명: {{ item.repairshop_name }}</VListItemTitle>
                <VListItemSubtitle>주소: {{ item.repairshop_address }}</VListItemSubtitle>
              </VListItemText>
              <VDivider></VDivider>
            </VListItem>
          </VList>
        </div>
      </VCol>
      <VCol cols="12">
        <VBtn color="primary" @click="filteredList(1)" class="mx-5 my-2">종합 정비소</VBtn>
        <VBtn color="info" @click="filteredList(2)" class="mx-5 my-2">소형차 정비소</VBtn>
        <VBtn color="warning" @click="filteredList(3)" class="mx-5 my-2">자동차전문 정비소</VBtn>
        <VBtn color="success" @click="aroundList()" class="mx-5 my-2">전체보기</VBtn>
        <VBtn color="secondary" @click="goMain()" class="mx-5 my-2">메인 페이지로</VBtn>
      </VCol>
    </VRow>
  </VCard>
</template>


<script>
export default {
  data() {
    return {
      aroundData: [],
      filteredData: [],
      markers: [], // 수정: 마커를 담을 배열
      rate: '',
      kakaoMap: null, // 수정: kakaoMap 변수 추가
      userLatitude: null,
      userLongitude: null,
    };
  },
  mounted() {
    this.kakaoMap = this.$refs.kakaoMap;

    navigator.geolocation.getCurrentPosition((position) => {
      this.userLatitude = position.coords.latitude;
      this.userLongitude = position.coords.longitude;

      const options = {
        center: new window.kakao.maps.LatLng(this.userLatitude, this.userLongitude),
        level: 3,
      };
      this.map = new window.kakao.maps.Map(this.kakaoMap, options);

      const markerPosition = new window.kakao.maps.LatLng(this.userLatitude, this.userLongitude);
      this.userMarker = new window.kakao.maps.Marker({
        position: markerPosition,
      });
      this.userMarker.setMap(this.map);
    });

    this.aroundList();
  },
  methods: {
    async aroundList() {
      try {
        // 수정: 이전 마커들 지우기
        this.markers.forEach(marker => marker.setMap(null));
        this.markers = [];

        const response = await axios.get('http://localhost:8888/repairshop');

        // 4km 반경 내의 데이터로 필터링
        this.filteredData = response.data.filter(item => {
          const itemLat = item.latitude;
          const itemLng = item.longitude;
          
          // 사용자 위치와 매장 위치 사이의 거리 계산
          const distance = this.calculateDistance(this.userLatitude, this.userLongitude, itemLat, itemLng);

          // 거리가 4km 이내만 선택
          return distance <= 4;
        });

        // 새로운 마커들 추가
        this.filteredData.forEach(item => {
          const markerPosition = new window.kakao.maps.LatLng(item.latitude, item.longitude);
          const marker = new window.kakao.maps.Marker({
            position: markerPosition,
          });

          // 인포윈도우에 표출될 내용 설정
          const iwContent = `<div style="padding:5px;">${item.repairshop_name}</div>`;

          // 인포윈도우 생성
          const infoWindow = new window.kakao.maps.InfoWindow({
            content: iwContent,
            removable: true, // 인포윈도우를 닫을 수 있는 x 버튼 표시
          });

          // 마커에 클릭 이벤트를 등록
          window.kakao.maps.event.addListener(marker, 'click', () => {
            // 마커 위에 인포윈도우를 표시
            infoWindow.open(this.map, marker);
          });

          marker.setMap(this.map);
          this.markers.push(marker); // 수정: 마커를 배열에 추가
        });
      } catch (error) {
        console.error('데이터 가져오기 오류:', error);
      }
    },

    async filteredList(rate) {
      try {
        // 수정: 이전 마커들 지우기
        this.markers.forEach(marker => marker.setMap(null));
        this.markers = [];

        const responseA = await axios.get(`http://localhost:8888/repairshop/rate/${rate.toString()}`);
        this.filteredData = responseA.data.filter(item => {
          const itemLat = item.latitude;
          const itemLng = item.longitude;          
          const distance = this.calculateDistance(this.userLatitude, this.userLongitude, itemLat, itemLng);
          return distance <= 4;
        });

        // 새로운 마커들 추가
        this.filteredData.forEach(item => {
          const markerPosition = new window.kakao.maps.LatLng(item.latitude, item.longitude);
          const marker = new window.kakao.maps.Marker({
            position: markerPosition,
          });

          // 인포윈도우에 표출될 내용 설정
          const iwContent = `<div style="padding:5px;">${item.repairshop_name}</div>`;

          // 인포윈도우 생성
          const infoWindow = new window.kakao.maps.InfoWindow({
            content: iwContent,
            removable: true, // 인포윈도우를 닫을 수 있는 x 버튼 표시
          });

          // 마커에 클릭 이벤트를 등록
          window.kakao.maps.event.addListener(marker, 'click', () => {
            // 마커 위에 인포윈도우를 표시
            infoWindow.open(this.map, marker);
          });

          marker.setMap(this.map);
          this.markers.push(marker); // 수정: 마커를 배열에 추가
        });
      } catch (error) {
        console.error('데이터 가져오기 오류:', error);
      }
    },

    calculateDistance(lat1, lon1, lat2, lon2) {
      // 하버사인 공식을 사용하여 두 지점 사이의 거리를 계산하는 함수
      const R = 6371; // 지구의 반지름 (단위: 킬로미터)
      const dLat = this.deg2rad(lat2 - lat1);
      const dLon = this.deg2rad(lon2 - lon1);
      const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      const distance = R * c; // 거리 (킬로미터 단위)
      return distance;
    },
    deg2rad(deg) {
      return deg * (Math.PI / 180);
    },    
    goToDetail(repairshop_code) {
      this.$router.push({ name: 'RepDetail', params: { repairshop_code } });
    },
    goMain() {
      this.$router.push({ name: 'main' });
    }
  },
};
</script>