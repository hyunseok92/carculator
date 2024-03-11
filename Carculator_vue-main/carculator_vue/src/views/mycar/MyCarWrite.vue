<script setup >
import axios from "axios";
import { onMounted, ref } from "vue";
import FloatingCard from "@/components/FloatingCard.vue";
import FIATimg from "@/views/mycar/FIAT.jpg";

const date = ref(null);
const ex8 = ref("primary");

const selected = ref();

</script>

<template>
<div>
  <VRow>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs :items="breadcrumbs" divider="-" class="px-0" />
    </VCol>

    <VCol cols="12" md="4">
      <FloatingCard
        :image="{ src: car_original_img || previewImg }"
        alt="car_original_img"
      >
      </FloatingCard>
    </VCol>

    <VCol cols="12" md="8">
      <VCard title="차량 등록">
        <VCardText>
          <VForm @submit.prevent="updateProduct">
            <VRow>
              <VCol cols="12">
                <VFileInput
                  v-model="previewImg"
                  placeholder="차량사진"
                  accept="image/png, image/jpeg, image/bmp"
                  label="사진을 선택해주세요."
                  ref="fileInputRef"
                  @change="handleFileUpload"
                />
              </VCol>
              <VCol cols="12">
                <VTextField
                  v-model="car_model"
                  placeholder="모델명"
                  label="모델명을 입력해주세요."
                />
              </VCol>
              <VCol cols="12">
                <VTextField
                  v-model="car_year"
                  placeholder="연식"
                  label="차량의 연식을 입력해주세요."
                />
              </VCol>
              <VCol cols="12">
                <VTextField
                  v-model="car_number"
                  placeholder="차량번호"
                  label="차량번호를 입력해주세요."
                  @input="numberValidation"
                />
                <v-alert v-if="!validatedCarNumber" type="error" outlined>
                  차량번호를 '12가 3456'의 형태로 입력해주세요.
                </v-alert>
              </VCol>

              <VCol cols="12">
                <VBtn
                  color="success"
                  variant="tonal"
                  class="me-4"
                  @click="registMyCar"
                >
                  등록하기
                </VBtn>

                <VBtn
                  variant="tonal"
                  color="error"
                  class="me-4"
                  @click="backToMyPage"
                >
                  등록 취소
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
  <VRow>
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
  </VRow>
</div>
</template>

<script>
import { onMounted } from "vue";

onMounted(() => {});

export default {
  data() {
    return {
      // car_id: 2,
      car_model: "",
      car_year: "",
      car_number: "",
      membercode: "",
      car_original_img: FIATimg,
      car_rename_img: null,
      previewImg: null,
      previewImg2: null,
      validatedCarNumber: true,
      processing: false,
    };
  },
  methods: {
    numberValidation() {
      const condition = /^\d{2,3}[가-힣]{1} \d{4}$/;
      this.validatedCarNumber = condition.test(this.car_number);
    },
    async handleFileUpload() {
      console.log("handleFileUpload 함수 진입!");
      this.processing = true;
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
        formData.append("membercode", this.membercode);

        alert(
          "AI 차량식별을 시작합니다.\n경우에 따라 최대 30초정도 소요됩니다."
        );
        
        try {
          const imgResponse = await axios.post(
            "http://localhost:8888/car/img",
            formData,
            {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }
          );
          this.car_model = imgResponse.data;
          alert(
            "AI가 차종식별을 마쳤습니다!\n차종이 일치한다면 나머지 내용을 작성해주세요."
          );
          this.processing = false;
        } catch (error) {
          alert(
            "AI 차량분석에 실패했습니다.\n차량 모델명을 직접 입력해주세요."
          );
          this.processing = false;
        }
      } else {
        console.log("Error");
      }
    },

    async registMyCar() {
      this.membercode = sessionStorage.getItem("memberCode");

      const formData = new FormData();

      formData.append("carImg", this.previewImg2);
      formData.append("car_model", this.car_model);
      formData.append("car_year", this.car_year);
      formData.append("car_number", this.car_number);
      formData.append("membercode", this.membercode);

      try {
        // axios를 사용하여 서버로 데이터를 전송
        const response = await axios.post(
          "http://localhost:8888/car",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        alert("차량등록이 완료되었습니다.");
        this.$router.push("/mycarInfo");
        // console.log("\n\n폼데이터 내용 : " + JSON.stringify(formData, null, 2));
      } catch (error) {
        alert("차량은 한대만 등록할 수 있습니다.");
      }
    },
    backToMyPage() {
      this.$router.back();
    },
  },
};
</script>

<style lang="scss">
</style>