<script setup >
import { onMounted, ref } from "vue";
import axios from "axios";
import avatar1 from "@/assets/avatars/avatar-1.png";

let reserv_code = null;

const reservation = [
  { text: "예약자명", icon: "mdi-account-outline", value: "" },
  { text: "연락처", value: "" },
  { text: "차량번호", value: "" },
  { text: "차종", value: "" },
  { text: "예약일시", icon: "mdi-calendar", value: "" },
  { text: "정비항목", icon: "mdi-cube-outline", value: "" },
  { text: "예약내용", icon: "mdi-file-document-outline", value: "" },
];

onMounted(async () => {
  const urlParams = new URLSearchParams(window.location.search);
  reserv_code = urlParams.get("code");
  await fetchReservation();
});

const fetchReservation = async () => {
  try {
    const response = await axios.get(
      "http://localhost:8888/reservation/" + reserv_code
    );
    console.log("예약번호 : " + reserv_code);
    const data = await response.data;

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
    reservation[4].value = formattedDate;
    reservation[5].value = data.repair_item;
    reservation[6].value = data.content;

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
          <VListItem title="접수된 예약 상세정보"></VListItem>
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

            <VListItemTitle>
              {{ item.text }} : {{ item.value }}
            </VListItemTitle>
          </VListItem>
        </VList>

        <div class="buttonsContainer">
          <div class="buttons">
            <VBtn
              @click="makeitYes"
              color="success"
              variant="tonal"
              style="margin-right: 5px; margin-bottom: 15px"
            >
              예약수락
            </VBtn>

            <VBtn
              @click="makeitNo"
              color="error"
              variant="tonal"
              style="margin-left: 5px; margin-bottom: 15px"
            >
              예약반려
            </VBtn>
          </div>
        </div>
      </div>
    </div></VCard
  >
</template>

<script>
export default {
  methods: {
    makeitYes() {
      const urlParams = new URLSearchParams(window.location.search);
      const code = urlParams.get("code");
      // console.log("수락에 필요한 예약코드 : " + code)
      const data = {
        reserv_yn: "예약완료",
        reserv_code: code,
      };
      console.log(data);
      axios
        .patch("http://localhost:8888/reservation", data)
        .then((response) => {
          alert("예약이 수락되었습니다.");
          this.$router.push("/reservList_shop");
        });
    },
    makeitNo() {
      const urlParams = new URLSearchParams(window.location.search);
      const code = urlParams.get("code");
      // console.log("반려에 필요한 예약코드 : " + code)
      const data = {
        reserv_yn: "예약반려",
        reserv_code: code,
      };
      console.log(data);
      axios
        .patch("http://localhost:8888/reservation", data)
        .then((response) => {
          alert("예약이 반려되었습니다.");
          this.$router.push("/reservList_shop");
        });
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