<script setup>
import axios from "axios";
import { ref } from "vue";

const isPasswordVisible = ref(true);
</script>

<template>
  <VList>
    <div class="wrapper" style="display: flex; justify-content: center; min-height: 75vh;">
      <div style="width: 30%; align-self: flex-start">
        <p style="text-align: center; margin-top: 120px;">회원님의 소중한 정보를 위해<br>
          다시 한 번 비밀번호를 입력해주세요.</p>
        <VCol cols="12" style="text-align: center;">
          <VTextField 
          v-model="member.pwd"
          :type="isPasswordVisible ? 'password' : 'text'"
            label="비밀번호"
            :append-inner-icon="
              isPasswordVisible ? 'mdi-eye-off-outline' : 'mdi-eye-outline'
            "
            class="mb-6"
            @click:append-inner="isPasswordVisible = !isPasswordVisible"
            @keyup.enter="toModifyPage"
          >
          </VTextField>
        </VCol>
        <VCol cols="12">
          <div style="text-align: center;">
            <VBtn @click="toModifyPage" color="success" variant="tonal" style="margin-right: 10px; width: 100px;">
              확인
            </VBtn>
            <VBtn @click="goBack" color="error" variant="tonal" style="margin-right: 10px; width: 100px;">
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
  data() {
    return {
      member: {
        membercode: null,
        pwd: null,
      }
    }
  },
  methods: {
    async toModifyPage() {
      try {
        this.member.membercode = sessionStorage.getItem("memberCode");
        await axios.post("http://localhost:8888/member/check-pw", this.member);
        this.$router.push("/changePassword");
      } catch(error) {
        alert("비밀번호가 맞지 않습니다.\n확인 후 다시 입력해주세요.");
        this.member.pwd = null;
        console.error(error);
      }
    },
    goBack() {
      this.$router.go(-1);
    }
  }
}
</script>

<style></style>