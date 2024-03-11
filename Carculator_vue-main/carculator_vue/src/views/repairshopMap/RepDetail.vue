
<script setup>
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
  {
    title: '정비소 상세정보',
    disabled: false,
  },
]
</script>

<template>
  <VCard>
  <VRow>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs
        :items="breadcrumbs"
        divider="-"
        class="px-0"
      />
    </VCol>

    <VCol cols="12">
        <VCardText>
          <VRow>
            <VCol
              cols="12"
              md="5"
            >
      <div class="kakaoMap" style="border: 1px solid brown; width: 500px; height: 400px;" ref="kakaoMap"></div>
      
            </VCol>

            <VCol
              cols="12"
              md="7"
            >
              <div class="ms-md-8">
                <h4 class="text-h5">
                  {{ repairshop_name }}
                </h4>

                <h6 class="text-base font-weight-medium mt-5">
                  영업시간
                </h6>
                <p class="mt-4">
                  {{ repairshop_open }} ~ {{ repairshop_close }}
                </p>

                <h6 class="text-base font-weight-medium mt-5">
                  전화번호
                </h6>
                <p class="mt-4">
                  {{ repairshop_phone }}
                </p>

                <h6 class="text-base font-weight-medium mt-5">
                  주소
                </h6>
                <VDivider class="my-3" />
                {{ repairshop_address }}
              </div>
              <div class="d-flex flex-wrap gap-4 mt-6">
                <VBtn color="info" @click="gobackList()">
                  정비소 리스트 보기
                </VBtn>

                <VBtn color="success" @click="reservation(repairshop_code)">
                  정비 예약하기
                </VBtn>
                
                <VBtn color="secondary" @click="gohome()">
                  메인 페이지로
                </VBtn>
              </div>
            </VCol>
          </VRow>
        </VCardText>
    </VCol>
  </VRow>
  </VCard>
</template>

  
<script>
import axios from 'axios';

export default {
  props: {
    repairshop_code: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      repairshop_name: '',
      repairshop_address: '',
      repairshop_phone: '',
      latitude: '',
      longitude: '',
      repairshop_open: '',
      repairshop_close: '',
      kakaoMap: null,
      marker: null,
    };
  },
  mounted() {
    this.kakaoMap = this.$refs.kakaoMap; // 먼저 this.kakaoMap을 초기화

    this.detailInfo(this.repairshop_code); // detailInfo 호출 이후에 this.latitude 및 this.longitude 설정
  },
  methods: {
    detailInfo(repairshop_code) {
      axios.get(`http://localhost:8888/repairshop/${repairshop_code}`)
        .then((res) => {
          this.repairshop_name = res.data.repairshop_name;
          this.repairshop_address = res.data.repairshop_address;
          this.repairshop_phone = res.data.repairshop_phone;
          this.latitude = res.data.latitude;
          this.longitude = res.data.longitude;
          this.repairshop_open = res.data.repairshop_open;
          this.repairshop_close = res.data.repairshop_close;

          // 여기서 지도 및 마커 초기화
          this.initializeMap();
        })
        .catch((err) => {
          console.error(err);
        });
    },
    initializeMap() {
      const options = {
        center: new window.kakao.maps.LatLng(this.latitude, this.longitude),
        level: 3,
      };
      const map = new window.kakao.maps.Map(this.kakaoMap, options);

      const markerPosition = new window.kakao.maps.LatLng(this.latitude, this.longitude);
      this.marker = new window.kakao.maps.Marker({
        position: markerPosition,
      });
      this.marker.setMap(map);
    },
    reservation(repairshop_code) {
      this.$router.push({ name: 'reservWrite_user', params: { repairshop_code } });
    },
    gohome() {
      this.$router.push({ name: 'main' });
    },
    gobackList() {
      this.$router.go(-1);
    },
  },
};
</script>
  