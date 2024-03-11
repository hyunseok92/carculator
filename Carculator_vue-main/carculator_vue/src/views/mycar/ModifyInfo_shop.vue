<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const membercode = sessionStorage.getItem("memberCode");
const repairshop = ref({
  membercode: membercode,
  shopname: "",
  shopaddress: "",
  shopphone: "",
});

onMounted(() => {
  fetchMyShop();
});

const fetchMyShop = async () => {
  try {
    const res = await axios.get("http://localhost:8888/member/" + membercode);
    const userData = await res.data;

    repairshop.value.shopname = userData.shopname;
    repairshop.value.shopaddress = userData.shopaddress;
    repairshop.value.shopphone = userData.shopphone;
  } catch (error) {
    console.error(error);
  }
};

const modifyMyShop = async () => {
    try {
        await axios.patch("http://localhost:8888/member/modify-shop", repairshop.value);
        alert("정비소 정보가 성공적으로 변경되었습니다.");
        router.push("/mycarInfo")
    } catch (error) {
        console.error(error);
    }
};
</script>

<template>
  <VList>
    <p style="font-size: 20px; text-align: center; margin-top: 15px">
      정비소 정보변경
    </p>
    <div class="wrapper" style="display: flex; justify-content: center">
      <div style="width: 40%; align-self: flex-start">
        <VCol cols="12">
          <p style="margin-bottom: 7px">사업장명</p>
          <VTextField
            v-model="repairshop.shopname"
            placeholder="사업장명을 입력해주세요."
            label="사업장명을 입력해주세요."
          />
          <p style="margin-top: 10px; margin-bottom: 7px">사업장 주소</p>
          <VTextField
            v-model="repairshop.shopaddress"
            placeholder="사업장 주소를 입력해주세요."
            label="사업장 주소를 입력해주세요."
          />
          <p style="margin-top: 10px; margin-bottom: 7px">사업장 전화번호</p>
          <VTextField
            v-model="repairshop.shopphone"
            placeholder="사업장 전화번호를 입력해주세요."
            label="사업장 전화번호를 입력해주세요."
          />
        </VCol>
        <VCol cols="12">
          <div style="text-align: center">
            <VBtn
              @click="modifyMyShop"
              color="success"
              variant="tonal"
              style="margin-right: 10px"
            >
              변경하기
            </VBtn>
            <VBtn
              @click="goBack"
              color="error"
              variant="tonal"
              style="margin-right: 10px"
            >
              변경취소
            </VBtn>
          </div>
        </VCol>
      </div>
    </div>
  </VList>
</template>

<script>
export default {
  methods: {
    // gotoInfoAgain() {
    //   this.$router.push("/mycarInfo");
    // },
    goBack() {
      this.$router.back();
    },
  },
};
</script>

<style>
</style>