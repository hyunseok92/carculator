<script setup >
import VueDatePicker from "@vuepic/vue-datepicker";
import axios from "axios";

const date = ref(null);
const ex8 = ref("primary");
</script>

<template>
  <VCard>
    <div class="wrapper" style="display: flex; justify-content: center">
      <div style="width: 50%; align-self: flex-start">
        <VCardText class="d-flex align-center justify-space-between">
          <div>
            <h4 class="text-h4">정비소 예약페이지</h4>
            <span class="font-weight-semibold" style="font-size: 16px">
              &nbsp;&nbsp;{{ repairshop_name }}</span
            >
          </div>
        </VCardText>
        <VDivider />
        <br />
        예약일시 지정
        <p style="font-size: 11px; margin: 0px">
          시계모양을 눌러 시간을 지정해주세요.
          <br /><strong>※ 시간단위로 예약됩니다.</strong>
        </p>
        <div class="wrapper" style="display: flex; justify-content: center">
          <div style="align-self: flex-start">
            <VueDatePicker
              v-model="reservation.reserv_date"
              inline
              auto-apply
              style="margin-top: 5px; margin-bottom: 10px"
            />
          </div>
        </div>
        정비항목 선택
        <VRadioGroup v-model="reservation.repair_item" inline>
          <VRadio
            v-model="reservation.repair_item"
            label="차체 및 일반부품"
            color="primary"
            value="차체 및 일반부품"
          />
          <VRadio
            v-model="reservation.repair_item"
            label="엔진 주요부품"
            color="primary"
            value="엔진 주요부품"
          />
          <VRadio
            v-model="reservation.repair_item"
            label="소모성 부품"
            color="primary"
            value="소모성 부품"
          />
          <VRadio
            v-model="reservation.repair_item"
            label="냉난방 부품"
            color="primary"
            value="냉난방 부품"
          />
        </VRadioGroup>

        예약자명
        <VTextField
          v-model="reservation.name"
          label="예약자명을 입력해주세요."
          style="margin-top: 10px; margin-bottom: 10px"
        />

        정비차종
        <VTextField
          v-model="reservation.car_model"
          label="정비할 차종을 입력해주세요."
          style="margin-top: 10px; margin-bottom: 10px"
        />

        차량번호
        <VTextField
          v-model="reservation.car_number"
          label="차량번호를 입력해주세요."
          @input="numberValidation"
          style="margin-top: 10px; margin-bottom: 10px"
        />
        <v-alert v-if="!validatedCarNumber" type="error" outlined>
          차량번호를 '12가 3456'의 형태로 입력해주세요.
        </v-alert>

        연락처
        <VTextField
          v-model="reservation.phone"
          label="연락처를 입력해주세요."
          style="margin-top: 10px; margin-bottom: 10px"
        />

        정비내용
        <VTextarea
          v-model="reservation.content"
          label="차량 상태 및 정비를 원하는 부분에 대한 설명을 입력해주세요."
          style="margin-top: 10px; margin-bottom: 10px"
        />

        <div class="wrapper" style="display: flex; justify-content: center">
          <div style="align-self: flex-start">
            <VBtn
              @click="commitReserv"
              color="success"
              variant="tonal"
              style="
                margin-right: 10px;
                margin-top: 20px;
                margin-bottom: 30px;
                width: 150px;
                height: 45px;
              "
            >
              예약하기
            </VBtn>
            <VBtn
              @click="backToList"
              color="error"
              variant="tonal"
              style="
                margin-left: 10px;
                margin-top: 20px;
                margin-bottom: 30px;
                width: 150px;
                height: 45px;
              "
            >
              예약취소
            </VBtn>
          </div>
        </div>
      </div>
    </div>
  </VCard>
</template>

<script>
export default {
  data() {
    return {
      reservation: {
        repairshop_code: "",
        car_number: "",
        reserv_date: null,
        repair_item: "",
        reserv_yn: "",
        content: "",
        membercode: "",
        phone: "",
        car_model: "",
      },
      validatedCarNumber: true,
      selectedDate: null,
      repairshop_name: "",
      repairshop_address: "",
      repairshop_phone: "",
      latitude: "",
      longitude: "",
      repairshop_open: "",
      repairshop_close: "",
    };
  },
  mounted() {
    this.getMemberInfo();
    this.detailInfo();
  },
  methods: {
    numberValidation() {
      const condition = /^\d{2,3}[가-힣]{1} \d{4}$/;
      this.validatedCarNumber = condition.test(this.reservation.car_number);
    },
    async getMemberInfo() {
      const membercode = sessionStorage.getItem("memberCode");
      try {
        const response = await axios.get(
          "http://localhost:8888/reservation/uInfo/" + membercode
        );
        const data = response.data;
        this.reservation.name = data.name;
        this.reservation.phone = data.phone;
        this.reservation.car_model = data.car_model;
        this.reservation.car_number = data.car_number;
      } catch (error) {
        console.error(error);
      }
    },
    async detailInfo() {
      try {
        const response = await axios.get(
          "http://localhost:8888/repairshop/" +
            this.$route.params.repairshop_code
        );
        console.log("전달온 값 : " + this.$route.params.repairshop_code);
        const data = response.data;
        console.log(data.value);
        this.reservation.repairshop_code = data.repairshop_code;
        this.repairshop_name = data.repairshop_name;
        this.repairshop_address = data.repairshop_address;
        this.repairshop_phone = data.repairshop_phone;
        this.latitude = data.latitude;
        this.longitude = data.longitude;
        this.repairshop_open = data.repairshop_open;
        this.repairshop_close = data.repairshop_close;
      } catch (error) {
        console.error(error);
      }
    },
    async commitReserv() {
      this.reservation.membercode = sessionStorage.getItem("memberCode");

      console.log(JSON.stringify(this.reservation, null, 2));
      try {
        // axios를 사용하여 서버로 데이터를 전송
        const response = await axios.post(
          "http://localhost:8888/reservation",
          this.reservation
        );

        alert(
          "예약이 완료되었습니다.\n정비업체에서 확인 후 예약이 확정됩니다."
        );
        this.$router.push("/reservList_user");
      } catch (error) {
        // 오류 처리
        alert("이미 이 시간에 예약이 있습니다.\n다른 예약시간을 선택해주세요.");
      }
    },
    backToList() {
      this.$router.go(-1);
    },
  },
};
</script>

<style lang="scss">
@use "@/styles/libs/vue-datepicker.scss";
</style>