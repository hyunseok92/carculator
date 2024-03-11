<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";

let membercode = null;

const car_id = ref(0);
const role = sessionStorage.getItem("userRole");
const currentUserData = ref([]);

const normalUser = [
  { text: "이름", value: "", icon: "mdi-account-outline" },
  { text: "이메일", value: "" },
  { text: "연락처", value: "" },
  { text: "성별", value: "" },
  { text: "생년월일", value: "" },
];

const shopUser = [
  { text: "사업장명", value: "" },
  { text: "사업장 주소", value: "" },
  { text: "사업장 전화번호", value: "" },
  { text: "사업주명", value: "", icon: "mdi-account-outline" },
  { text: "이메일", value: "" },
  { text: "연락처", value: "" },
  { text: "성별", value: "" },
  { text: "생년월일", value: "" },
];

const mycar = [
  { text: "차주명", value: "" },
  { text: "차종", value: "" },
  { text: "차량번호", value: "" },
  { text: "차량연식", value: "" },
];

onMounted(async () => {
  membercode = sessionStorage.getItem("memberCode");
  await fetchMyInfo();
  await fetchMyCar();
});

const formatBitrhData = (birthDate) => {
  const cleaned = ("" + birthDate).replace(/\D/g, "");
  const match = cleaned.match(/^(\d{4})(\d{2})(\d{2})$/);

  if (match) {
    return match[1] + "년 " + match[2] + "월 " + match[3] + "일";
  }

  return birthDate;
};

const fetchMyInfo = async () => {
  try {
    const res = await axios.get("http://localhost:8888/member/" + membercode);
    const userData = await res.data;

    const originalBirth = userData.birth;
    const formattedBirth = formatBitrhData(originalBirth);

    // 일반유저 정보 매핑
    normalUser[0].value = userData.name;
    normalUser[1].value = userData.email;
    normalUser[2].value = userData.phone;
    normalUser[3].value = userData.gender;
    normalUser[4].value = formattedBirth;

    // 정비소유저 정보 매핑
    shopUser[0].value = userData.shopname;
    shopUser[1].value = userData.shopaddress;
    shopUser[2].value = userData.shopphone;
    shopUser[3].value = userData.name;
    shopUser[4].value = userData.email;
    shopUser[5].value = userData.phone;
    shopUser[6].value = userData.gender;
    shopUser[7].value = formattedBirth;

    if (userData.grade === "ROLE_USER") {
      currentUserData.value = normalUser;
    } else if (userData.grade === "ROLE_SHOP") {
      currentUserData.value = shopUser;
    }

    document.querySelector(".userItem:nth-child(1)").click();
    document.querySelector(".userItem:nth-child(1)").click();
  } catch (error) {
    console.log("에러 내용 : " + error);
  }
};

const fetchMyCar = async () => {
  try {
    const response = await axios.get("http://localhost:8888/car/" + membercode);
    const data = await response.data;
    console.log(data);

    mycar[0].value = data.name;
    mycar[1].value = data.car_model;
    mycar[2].value = data.car_number;
    mycar[3].value = data.car_year;

    car_id.value = data.car_id;

    document.querySelector(".mycarItem:nth-child(1)").click();
    document.querySelector(".mycarItem:nth-child(1)").click();
  } catch (error) {
    console.error("\nError : " + error);
  }
};
</script>


<template>
  <VCard>
    <div class="wrapper" style="display: flex; justify-content: center">
      <div style="width: 50%; align-self: flex-start">
        <VDivider />
        <VList>
          <VListItem :prepend-avatar="avatar1" title="나의 정보" subtitle="">
            <template #append> </template>
          </VListItem>
        </VList>

        <VDivider />

        <VList :lines="false" density="compact" nav>
          <VListItem
            v-for="(item, i) in currentUserData"
            :key="i"
            :value="item"
            color="primary"
            class="userItem"
          >
            <template #prepend>
              <VIcon :icon="item.icon" />
            </template>

            <VListItemTitle>
              {{ item.text }} : {{ item.value }}
            </VListItemTitle>
          </VListItem>
          <div class="buttonWrapper" style="text-align: center">
            <VBtn
              v-if="role === 'ROLE_SHOP'"
              @click="modifyShopInfo"
              color="success"
              variant="outlined"
              style="margin-top: 10px; margin-bottom: 5px; margin-right: 5px"
            >
              정비소 정보수정
            </VBtn>

            <VBtn
              @click="changePassword"
              color="success"
              variant="outlined"
              style="margin-top: 10px; margin-bottom: 5px; margin-left: 5px"
            >
              비밀번호 변경
            </VBtn>
          </div>
        </VList>

        <VDivider />

        <VList>
          <VListItem title="나의 차량정보">
            <template #append> </template>
          </VListItem>
        </VList>

        <VDivider />

        <VList :lines="false" density="compact" nav>
          <VListItem
            v-for="(item, i) in mycar"
            :key="i"
            :value="item"
            color="primary"
            class="mycarItem"
          >
            <template #prepend>
              <VIcon :icon="item.icon" />
            </template>

            <VListItemTitle>
              {{ item.text }} : {{ item.value }}
            </VListItemTitle>
          </VListItem>
          <div class="buttonsContainer">
            <div class="buttons" style="text-align: center">
              <VBtn
                @click="$router.push('/mycarWrite')"
                color="success"
                variant="outlined"
                style="margin-bottom: 10px; margin-top: 10px; margin-right: 5px"
              >
                차량등록
              </VBtn>
              <VBtn
                @click="deleteMyCar"
                color="error"
                variant="outlined"
                style="margin-bottom: 10px; margin-top: 10px; margin-left: 5px"
              >
                차량삭제
              </VBtn>
            </div>
          </div>
        </VList>
        <div>
          <div style="text-align: center">
            <VBtn
              @click="$router.push('/main')"
              color="success"
              variant="tonal"
              style="margin-top: 10px; margin-bottom: 15px; width: 150px; height: 40px"
            >
              홈으로
            </VBtn>
          </div>
        </div>
      </div>
    </div>
  </VCard>
</template>

<script>
export default {
  methods: {
    modifyShopInfo() {
      this.$router.push("/modifyInfo_shop");
    },
    movePage() {
      this.$router.push("/main");
    },
    async deleteMyCar() {
      const membercode = sessionStorage.getItem("memberCode");
      if (
        confirm(
          "삭제한 차량정보는 되돌릴 수 없습니다.\n삭제를 진행하시겠습니까?"
        )
      ) {
        try {
          const response = await axios.delete(
            "http://localhost:8888/car/" + membercode
          );
          alert("차량 정보가 삭제되었습니다.");

          if (response.status === 200) {
            this.movePage();
          } else {
            alert(
              "등록된 차량이 없거나 삭제 과정에서 문제가 발생했습니다.\n계속 이런 현상이 발생한다면 고객센터에 문의해주세요."
            );
          }
        } catch (error) {
          alert(
            "등록된 차량이 없거나 삭제 과정에서 문제가 발생했습니다.\n계속 이런 현상이 발생한다면 고객센터에 문의해주세요."
          );
          this.$router.push("/mycarInfo");
        }
      }
    },
    changePassword() {
      this.$router.push("/check-password");
    },
  },
};
</script>

<style>
</style>