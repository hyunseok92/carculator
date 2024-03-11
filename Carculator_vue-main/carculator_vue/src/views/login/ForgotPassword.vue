<script setup>
import authBgDark from '@/assets/pages/auth-bg-dark.svg'
import authBgLight from '@/assets/pages/auth-bg-light.svg'
import authForgotPasswordImg from '@/assets/pages/girl-forgot-something.png'
import { useTheme } from 'vuetify'
import { VForm } from 'vuetify/components/VForm'
import axios from "axios";


const forgetPasswordForm = ref()
const theme = useTheme()
const authBgThemeVariant = computed(() => {
  return theme.current.value.dark ? authBgDark : authBgLight
})
</script>

<template>
  <div class="auth-wrapper">
    <VCard
      max-width="900"
      min-width="350"
      class="fixed-card-size"
    >
      <VRow no-gutters>
        <VCol
          md="6"
          cols="12"
          class="border-e pa-sm-8 pa-4"
        >
          <VCardItem>
            <VCardTitle>비밀번호를 잊으셨나요?</VCardTitle>
          </VCardItem>

          <VCardText>
            <p>이메일을 입력하세요</p>

            <VForm ref="forgetPasswordForm">
              <VRow>
                <VCol cols="12">
                  <div class="d-flex mb-6">
                  <VTextField
                    v-model="member.email"
                    label="Email"
                    class="mr-4"
                  />

                  <VBtn
                    color="primary"
                    @click="sendEmailVerification"
                    style="height: 47px"
                  >
                    이메일 인증
                  </VBtn>
                  </div>

                  <div v-show="isVerificationCodeSent" class="row">
                  <div class="d-flex mb-6">
                    <VTextField
                      v-model="member.verificationCode"
                      label="인증 코드"
                      :rules="[(v) => !!v || '인증 코드를 입력하세요']"
                      class="mr-4"
                    />
                    <VBtn
                      color="primary"
                      @click="checkEmailCode"
                      style="height: 47px"
                    >
                      인증 확인
                    </VBtn>
                  </div>
                </div>
                </VCol>

                <VCol cols="12">
                  <VBtn
                    block
                    variant="text"
                    color="secondary"
                    size="large"
                    prepend-icon="mdi-chevron-double-left"
                    :to="{ name: 'home' }"
                  >
                    Back to login
                  </VBtn>
                </VCol>
              </VRow>
            </VForm>
          </VCardText>
        </VCol>

        <VCol
          cols="6"
          class="pa-8 text-center d-none d-md-block"
          :style="`background-image:url(${authBgThemeVariant});`"
        >
          <div class="d-flex align-center justify-center">
            <img
              width="280"
              :src="authForgotPasswordImg"
            >
          </div>
        </VCol>
      </VRow>
    </VCard>
  </div>
</template>

<script>
export default {
  data() {
    return {
      member: {
        email: null,
        verificationCode: null,
      },

      isVerificationCodeSent: false, //이메일 인증 코드 전송 여부
      isAllInputCompleted: false, //모든 입력이 완료되었는지 여부
    };
  },
   methods: {
    async sendEmailVerification() {
      try {
        //이메일 전송 로직
        const response = await axios.post(
          "http://localhost:8888/forgotpassword/" + this.member.email);
        if (response.status === 200) {
          alert("이메일 인증 메일이 전송되었습니다.");
          this.isVerificationCodeSent = true;
          this.checkEmail = response.data;
          console.log(this.checkEmail);
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          // 서버에서 온 에러 응답
          alert("이미 등록한 이메일이 있습니다.");
          this.member.email = "";
        } else {
          // 네트워크 오류 등 기타 오류
          console.error("Error sending email verification:", error);
           alert("이메일 전송 오류");
          this.member.email = "";
        }
      }
    },
    async checkEmailCode() {
      try {
        // 사용자가 입력한 코드와 서버에서 받아온 코드 비교
        if (this.checkEmail == this.member.verificationCode) {
          alert("이메일 인증 성공");

          const memberEmail = this.member.email;
          this.$router.push({ path: '/forgotchangepwd', query: { email : memberEmail } });
        } else {
          alert("이메일 인증 실패");
          this.member.verificationCode = "";
        }
      } catch (error) {
        console.error("Error checking email code:", error);
      }
    },
  },
};
</script>

