<script setup >
import axios from "axios";
import { onMounted, ref } from "vue";
import FloatingCard from "@/components/FloatingCard.vue";
import FIATimg from "@/views/mycar/FIAT.jpg";

const date = ref(null);
const ex8 = ref("primary");

const selected = ref();
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '차량 파손조회 AI',
    disabled: false,
  },
]
</script>

<template>
  <VRow>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs :items="breadcrumbs" divider="-" class="px-0" />
    </VCol>

    <VCol cols="12" md="4">
      <FloatingCard :image="{ src: car_original_img || previewImg }" alt="car_original_img">
      </FloatingCard>
    </VCol>

    <VCol cols="12" md="8">
      <VCard title="차량 파손정보 조회">
        <VCardText>
          <VForm @submit.prevent="updateProduct">
            <VRow>
              <VCol cols="12">
                <VFileInput v-model="previewImg" placeholder="차량사진" accept="image/png, image/jpeg, image/bmp"
                  label="사진을 선택해주세요." ref="fileInputRef" @change="handleFileUpload" />
              </VCol>

              <VCol cols="12">
                <VTextField v-model="car_damaged" placeholder="파손정보" label="차량 파손정보" :readonly="false" />
              </VCol>

              <VCol cols="12">
                <VTextField v-model="rep_Cost" placeholder="예상 정비금액" label="예상 정비금액" :readonly="false" />
              </VCol>

              <VCol cols="12">
                <VBtn color="success" variant="tonal" class="me-4" @click="searchDamagedCar">
                  파손 조회하기
                </VBtn>
                <VBtn color="info" variant="tonal" class="me-4" @click="goRepsMap">정비 예약하기</VBtn>
                <VBtn variant="tonal" color="error" class="me-4" @click="backToMain">
                  메인으로
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
  <VCol>
    <div style="display: flex; justify-content: center">
      <div
        class="d-flex align-center gap-4 flex-wrap"
      style="margin-top: 20px; align-self: flex-start"
      >
      <VProgressCircular indeterminate color="info"
      v-if="processing"/>
      </div>
    </div>
  </VCol>
</template>

<script>
export default {
  data() {
    return {
      // car_id: 2,
      car_damaged: "",
      membercode: "",
      rep_Cost: "",
      car_original_img: FIATimg,
      car_rename_img: null,
      previewImg: null,
      previewImg2: null,
      damageStr: "",
      totalcost: 0,
      processing: false,
    };
  },
  methods: {
    async handleFileUpload() {
      this.totalcost = 0;
      console.log("handleFileUpload 함수 진입!");
      const fileInput = this.$refs.fileInputRef;
      this.previewImg2 = fileInput ? fileInput.files[0] : null;
      if (this.previewImg2 && this.previewImg2.size > 0) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.car_original_img = e.target.result;
        };
        reader.readAsDataURL(this.previewImg2);
        const formData = new FormData();
        formData.append("carImg", this.previewImg2);
      } else {
        console.log("Error");
      }
    },
    async searchDamagedCar() {
    console.log("searchDamagedCar 함수 진입!");
    alert("차량 파손정보 조회를 시작합니다.\n5초정도 소요됩니다. 잠시만 기다려주세요.");
    this.processing = true;
    const formData = new FormData();
    formData.append("carImg", this.previewImg2);
    try {
        const response = await axios.post("http://localhost:8888/damage/img", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
        console.log(response.data);
        alert("차량 파손정보 조회가 완료되었습니다.");
        this.processing = false;
        if (response.data.trim() === "") {
            this.car_damaged = "손상정보 없음";
            this.rep_Cost = "예상 수리금액 : " + this.totalcost + "원";
            return;
        }
        let damageStr = "";
        let dmgcount = 0;
        let dmg_category = "";
        const spdata = response.data.split(", ");

        console.log("\n\n" + spdata);

        for (let i = 0; i < spdata.length; i++) {
            const sdata = spdata[i].split(" ");
            for (let j = 0; j < sdata.length; j++) {
                if (j == 0) {
                    damageStr += sdata[j] + "개의 ";
                    dmgcount = sdata[j];
                } else {
                    switch (sdata[j]) {
                        case "Breakage":
                            dmg_category = "부서짐" + " 발견됨.\n";
                            this.totalcost += dmgcount * 500000;
                            break;
                        case "Crushed":
                            dmg_category = "찌그러짐" + " 발견됨.\n";
                            this.totalcost += dmgcount * 300000;
                            break;
                        case "Scratched":
                            dmg_category = "긁힘" + " 발견됨.\n";
                            this.totalcost += dmgcount * 50000;
                            break;
                        case "Scratcheds":
                            dmg_category = "긁힘" + " 발견됨.\n";
                            this.totalcost += dmgcount * 50000;
                            break;
                        case "Separated":
                            dmg_category = "이격" + " 발견됨.\n";
                            this.totalcost += dmgcount * 1000000;
                            break;
                    }
                    damageStr += dmg_category;
                }
            }
        }
        this.rep_Cost = "예상 수리금액 : " + this.totalcost + "원";
        this.car_damaged = damageStr;
    } catch (error) {
        console.log(error);
        this.car_damaged = "손상 정보를 가져오는 동안 에러 발생";
    }
},

    goRepsMap() {
      this.$router.push("RepsMap");
    },
    backToMain() {
      this.$router.push("main");
    },
  },
};
</script>
