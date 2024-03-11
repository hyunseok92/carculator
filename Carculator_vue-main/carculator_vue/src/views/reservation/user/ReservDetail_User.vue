<script setup >
import { onMounted, ref } from "vue";
import axios from "axios";
import avatar1 from "@/assets/avatars/avatar-1.png";

let reserv_code = ref(null);

const reservation = [
  { text: "예약자명", icon: "mdi-account-outline", value: "" },
  { text: "연락처", value: "" },
  { text: "차량번호", value: "" },
  { text: "차종", value: "" },
  { text: "정비소명", value: "", icon: "mdi-cog-outline" },
  { text: "주소", value: "", icon: "mdi-flag-outline" },
  { text: "예약일시", icon: "mdi-clock-outline", value: "" },
  { text: "정비항목", icon: "mdi-cube-outline", value: "" },
  { text: "예약내용", icon: "mdi-file-document-outline", value: "" },
];

onMounted(async () => {
  const urlParams = new URLSearchParams(window.location.search);
  console.log("상세페이지에서 예약번호 확인 : " + urlParams.get("code"));
  reserv_code.value = urlParams.get("code");
  await fetchReservation();
});

const fetchReservation = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8888/mdReservation/" + reserv_code.value
    );

    const data = await response.data;

    console.log("예약 번호 확인!!! : " + reserv_code.value);
    const dateString = data.reserv_date;
    const dateObj = new Date(dateString);

    const year = dateObj.getFullYear().toString().slice(-2);
    const month = (dateObj.getMonth() + 1).toString().padStart(2, "0");
    const date = dateObj.getDate().toString().padStart(2, "0");
    const hour = dateObj.getHours().toString().padStart(2, "0");
    const minute = dateObj.getMinutes().toString().padStart(2, "0");

    const formattedDate = `${year}.${month}.${date}. ${hour}시`;

    reservation[0].value = data.name;
    reservation[1].value = data.phone;
    reservation[2].value = data.car_number;
    reservation[3].value = data.car_model;
    reservation[4].value = data.repairshop_name;
    reservation[5].value = data.repairshop_address;
    reservation[6].value = formattedDate;
    reservation[7].value = data.repair_item;
    reservation[8].value = data.content;

    document.querySelector(".reservItem:nth-child(1)").click();
    document.querySelector(".reservItem:nth-child(1)").click();
  } catch (error) {
    console.log("Error\n", error);
  }
};
</script>

<template>
<VCard>
  <div class="wrapper" style="display: flex; justify-content: center">
    <div style="width: 50%; align-self: flex-start">
      
      <VList>
        <VListItem title="나의 예약 상세정보"> </VListItem>
      </VList>

      <VDivider />

      <VList :lines="false" density="compact" nav>
        <VListItem
          v-for="(item, i) in reservation"
          :key="i"
          :value="item"
          color="primary"
          class="reservItem"
        >
          <template #prepend>
            <VIcon :icon="item.icon" />
          </template>

          <VListItemTitle> {{ item.text }} : {{ item.value }} </VListItemTitle>
        </VListItem>
        <div class="buttonsContainer">
          <div class="buttons">
            <VBtn
              @click="toList"
              color="success"
              variant="tonal"
              style="margin-right: 5px"
            >
              목록으로
            </VBtn>

            <VBtn
              @click="cancelReservation"
              color="error"
              variant="tonal"
              style="margin-left: 5px"
            >
              예약취소
            </VBtn>
          </div>
        </div>
      </VList>
    </div>
  </div>
</VCard>
</template>

<script>
export default {
  methods: {
    toList() {
      this.$router.go(-1);
    },
    cancelReservation() {
      const urlParams = new URLSearchParams(window.location.search);
      const reserv_code = urlParams.get("code");
      console.log("전송할 예약코드" + reserv_code);
      if (
        confirm("취소한 예약은 되돌릴 수 없습니다.\n취소를 진행하시겠습니까?")
      ) {
        console.log("통신에 필요한 예약코드 : " + reserv_code);
        axios
          .delete("http://localhost:8888/reservation/" + reserv_code)
          .then((response) => {
            alert("예약이 취소되었습니다.");
            this.$router.push("/reservList_user");
          })
          .catch((error) => {
            console.error("예약 취소 중 오류 발생:", error);
            alert("예약 취소 중 오류가 발생했습니다.");
          });
      } else {
      }
    },
  },
};
</script>

<style lang="scss">
.buttonsContainer {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}
.buttons {
  align-self: flex-start;
}
</style>