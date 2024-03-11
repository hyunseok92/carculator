<script setup>
import axios from "axios";
import { ref } from "vue";

const isPasswordVisible = ref(true);
</script>

<template>
  <VList>
    <p style="font-size: 20px; text-align: center; margin-top: 15px">
      비밀번호 변경
    </p>
    <div class="wrapper" style="display: flex; justify-content: center">
      <div style="width: 40%; align-self: flex-start">
        <VCol cols="12">
          <p style="margin-bottom: 7px">변경할 비밀번호</p>
          <VTextField
            v-model="member.pwd"
            :type="isPasswordVisible ? 'password' : 'text'"
            label="비밀번호"
            :rules="[
              (v) => !!v || '비밀번호를 입력하세요',
              (v) =>
                /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(
                  v
                ) || '영문, 숫자, 특수문자를 조합하여 입력해주세요(8-16자)',
            ]"
            :append-inner-icon="
              isPasswordVisible ? 'mdi-eye-off-outline' : 'mdi-eye-outline'
            "
            class="mb-6"
            @click:append-inner="isPasswordVisible = !isPasswordVisible"
          />
          <p style="margin-bottom: 7px">비밀번호 확인</p>
          <VTextField
            v-model="member.pwd1"
            :type="isPasswordVisible ? 'password' : 'text'"
            label="비밀번호 확인"
            :rules="[
              (v) => !!v || '비밀번호를 입력하세요',
              (v) => v === member.pwd || '비밀번호가 일치하지 않습니다.',
            ]"
            :append-inner-icon="
              isPasswordVisible ? 'mdi-eye-off-outline' : 'mdi-eye-outline'
            "
            class="mb-6"
            @click:append-inner="isPasswordVisible = !isPasswordVisible"
          />
        </VCol>
        <VCol cols="12">
          <div style="text-align: center">
            <VBtn
              @click="modifyPassword"
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
  data() {
    return {
      member: {
        membercode: null,
        pwd: null,
      },
    };
  },
  methods: {
    async modifyPassword() {
      try {

      this.member.membercode = sessionStorage.getItem("memberCode");

      await axios.patch("http://localhost:8888/member", this.member);
      alert("비밀번호가 변경되었습니다.\n새로운 비밀번호로 다시 로그인해주세요.");
      this.$router.push("/")
      } catch (error) {
        alert("이전 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
      }
    },
    goBack() {
      this.$router.back();
    }
  },
};
</script>

<style>
</style>