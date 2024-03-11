<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from 'vue-router';
import axios from "axios";


const currentUserData = ref([]);
let status = ref();
let membercode = ref();
const route = useRouter();

const member = ref({
  membercode: membercode.value,
  status: status.value
});

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
  { text: "사업자 등급", value: "" },
  { text: "사업주명", value: "", icon: "mdi-account-outline" },
  { text: "이메일", value: "" },
  { text: "연락처", value: "" },
  { text: "성별", value: "" },
  { text: "생년월일", value: "" },
];


onMounted(async () => {
  await fetchMyInfo();
});

const formatBitrhData = (birthDate) => {
  const cleaned = ("" + birthDate).replace(/\D/g, "");
  const match = cleaned.match(/^(\d{4})(\d{2})(\d{2})$/);

  if (match) {
    return match[1] + "년 " + match[2] + "월 " + match[3] + "일";
  }

  return birthDate;
};

watch([membercode, status], () => {
  member.value = {
    membercode: membercode.value,
    status: status.value
  };
});

const fetchMyInfo = async () => {
  try {

    const urlParams = new URLSearchParams(window.location.search);
    membercode.value = urlParams.get("membercode");

    const res = await axios.get("http://localhost:8888/member/" + membercode.value);
    const userData = await res.data;

    // const originalPhone = userData.phone;
    // const formattedPhone = formatPhoneNumber(originalPhone);
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
    shopUser[3].value = userData.shoprate;
    shopUser[4].value = userData.name;
    shopUser[5].value = userData.email;
    shopUser[6].value = userData.phone;
    shopUser[7].value = userData.gender;
    shopUser[8].value = formattedBirth;

    status.value = userData.status;

    if (userData.grade === "ROLE_USER") {
      currentUserData.value = normalUser;
    } else if (userData.grade === "ROLE_SHOP") {
      currentUserData.value = shopUser;
    }

    // document.querySelector(".userItem:nth-child(1)").click();
    // document.querySelector(".userItem:nth-child(1)").click();
  } catch (error) {
    console.log("에러 내용 : " + error);
  }
};

const ApproveoClick =  async () => {
      try {

        member.value.status = 0;

        console.log("member value" + member.value.membercode);
        console.log("member status" + member.value.status);
        const response = await axios.post(
          "http://localhost:8888/member/shopStatus", member.value
        );

        if (response.status == 200) {
          alert("등록 완료");
          route.push({ name: 'memberSelect' });
        } else if(response.status == 400) {
          alert("업체 등록 실패");
        }


      } catch (error) {
        console.error("Error shop approve login click", error);
      }
}

const RefuseClick =  async () => {
      try {

        member.value.status = 1;

        console.log("member value" + member.value.membercode);
        console.log("member status" + member.value.status);
        const response = await axios.post(
          "http://localhost:8888/member/shopStatus", member.value
        );

        if (response.status == 200) {
          alert("수정 완료");
          route.push({ name: 'memberSelect' });
        }

      } catch (error) {
        console.error("Error shop refuse login click", error);
      }
}
</script>


<template>
  <VDivider />
  <VList>
    <VListItem :prepend-avatar="avatar1" title="회원 정보" subtitle="">
      <template #append>

      </template>
    </VListItem>
  </VList>
  <VDivider />

  <VList :lines="false" density="compact" nav>
    <VListItem v-for="(item, i) in currentUserData" :key="i" :value="item" color="primary" class="userItem">
      <template #prepend>
        <VIcon :icon="item.icon" />
      </template>

      <VListItemTitle> {{ item.text }} : {{ item.value }} </VListItemTitle>
    </VListItem>
  </VList>

  <div class="buttonWrapper" style="text-align: center">
    <VBtn v-if="status == '2'" @click="ApproveoClick" color="success" variant="outlined"
      style="margin-top: 10px; margin-bottom: 5px; margin-right: 5px">
      승인
    </VBtn>

    <VBtn v-if="status == '2'" @click="RefuseClick" color="error" variant="outlined"
      style="margin-top: 10px; margin-bottom: 5px; margin-left: 5px">
      거절
    </VBtn>
  </div>

  <VDivider />
  <div>
    <div style="text-align:center;">
      <VBtn @click="$router.push({ name: 'memberSelect' })" color="success" variant="tonal"
        style="margin-top: 20px; width:150px; height:40px;">
        목록으로
      </VBtn>
    </div>
  </div>
</template>