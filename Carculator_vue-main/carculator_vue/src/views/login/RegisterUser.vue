<script setup>
import authBgDark from "@/assets/pages/auth-bg-dark.svg";
import authBgLight from "@/assets/pages/auth-bg-light.svg";
import authRegisterImg from "@/assets/pages/working-laptop-while-sitting-chair.png";
import profileDefault from "@/assets/member/profileDefault.png";
import { useTheme } from "vuetify";
import axios from "axios";

const registerForm = ref();
const isPasswordVisible = ref(true);
const isContentExpand = ref(true);
const theme = useTheme();
const checkEmail = ref();

const authBgThemeVariant = computed(() => {
  return theme.current.value.dark ? authBgDark : authBgLight;
});
</script>

<template>
  <div class="auth-wrapper">
    <VCard max-width="2000" min-width="1000">
      <VRow no-gutters>
        <VCol md="6" cols="12" class="border-e pa-sm-8 pa-4">
          <VCardItem>
            <VCardTitle>일반 회원가입</VCardTitle>
          </VCardItem>

          <VCardText class="pb-0">
            <VForm ref="registerForm" @submit.prevent="() => {}">
              <div v-show="isContentExpand">
                <div class="d-flex mb-6">
                  <VTextField
                    v-model="member.email"
                    label="이메일"
                    :rules="[
                      (v) => !!v || '이메일을 입력하세요',
                      (v) =>
                        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) ||
                        '올바른 이메일 형식을 입력하세요',
                    ]"
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

                <VTextField
                  v-model="member.pwd"
                  :type="isPasswordVisible ? 'password' : 'text'"
                  label="비밀번호"
                  :rules="[
                    (v) => !!v || '비밀번호를 입력하세요',
                    (v) =>
                      /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(
                        v
                      ) ||
                      '영문, 숫자, 특수문자를 조합하여 입력해주세요(8-16자)',
                  ]"
                  :append-inner-icon="
                    isPasswordVisible
                      ? 'mdi-eye-off-outline'
                      : 'mdi-eye-outline'
                  "
                  class="mb-6"
                  @click:append-inner="isPasswordVisible = !isPasswordVisible"
                />

                <VTextField
                  v-model="member.pwd1"
                  :type="isPasswordVisible ? 'password' : 'text'"
                  label="비밀번호 확인"
                  :rules="[
                    (v) => !!v || '비밀번호를 입력하세요',
                    (v) => v === member.pwd || '비밀번호가 일치하지 않습니다.',
                  ]"
                  :append-inner-icon="
                    isPasswordVisible
                      ? 'mdi-eye-off-outline'
                      : 'mdi-eye-outline'
                  "
                  class="mb-6"
                  @click:append-inner="isPasswordVisible = !isPasswordVisible"
                />

                <VTextField
                  v-model="member.name"
                  label="이름"
                  :rules="[(v) => !!v || '이름을 입력하세요']"
                  class="mb-6"
                />

                <VTextField v-model="member.phone" label="전화번호(ex 010-1234-5678)" :rules="[
                  (v) => !!v || '전화번호를 입력하세요',
                  (v) => /^[0-9-]*$/.test(v) || '숫자와 하이픈(-)만 입력하세요',
                ]" class="mb-6" />

                <VTextField
                  v-model="member.birth"
                  label="생년월일(YYYYMMDD)"
                  :rules="[
                    (v) => !!v || '생년월일을 입력하세요',
                    (v) =>
                      /^[0-9]{8}$/.test(v) || '올바른 생년월일을 입력하세요',
                  ]"
                  class="mb-6"
                />

                <VSelect
                  v-model="member.gender"
                  :items="['남자', '여자']"
                  label="성별"
                  :rules="[(v) => !!v || '성별을 선택하세요']"
                  class="mb-6"
                />

                <VBtn
                  block
                  type="submit"
                  color="primary"
                  @click="submitUserInfo"
                  :disabled="!isAllInputCompleted"
                  style="height: 47px"
                >
                  회원가입
                </VBtn>
              </div>
            </VForm>
          </VCardText>
        </VCol>

        <VCol
          cols="6"
          class="pa-8 text-center d-none d-md-block"
          :style="`background-image:url(${authBgThemeVariant});`"
        >
          <div class="d-flex align-center justify-center">
            <img width="280" :src="authRegisterImg" />
          </div>

          <h6 class="text-body-1 font-weight-semibold mb-3">
            이미 회원이십니까 ?
          </h6>
          <p class="text-body-2">로그인 페이지로 이동합니다</p>
          <VBtn
            variant="outlined"
            color="primary"
            :to="{ name: 'home' }"
            append-icon="mdi-login"
          >
            로그인
          </VBtn>
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
        pwd: null,
        pwd1: null,
        name: null,
        phone: null,
        birth: null,
        gender: null,
        email: null,
        grade: "ROLE_USER",
        status: 0,
        type: h,
        verificationCode: null,
      },

      isVerificationCodeSent: false, //이메일 인증 코드 전송 여부
      isAllInputCompleted: false, //모든 입력이 완료되었는지 여부
    };
  },
  watch: {
    // 모든 입력 값이 변경될 때마다 호출되는 watch
    member: {
      handler() {
        // 모든 입력 값이 존재하는지 확인
        this.isAllInputCompleted = Object.values(this.member).every(
          (value) => value !== null && value !== undefined
        );

        console.log(this.member);
      },
      deep: true, // 중첩된 객체의 변경을 감지
    },
  },
  methods: {
    async submitUserInfo() {
      try {
        // axios를 사용하여 서버로 데이터를 전송
        const response = await axios.post(
          "http://localhost:8888/join",
          this.member
        );

        console.log(response.data);

        if (response.data.email != null) {
          alert("회원 가입 성공");
          this.$router.push({ name: "home" });
        } else {
          alert("회원 가입 실패");
        }
      } catch (error) {
        // 오류 처리
        if (error.response && error.response.status === 400) {
          // 서버에서 온 에러 응답
          alert("이미 등록된 이메일 입니다");
        }
      }
    },
    async sendEmailVerification() {
      try {
        //이메일 전송 로직
        const response = await axios.post(
          "http://localhost:8888/email/" + this.member.email,
          {}
        );
        this.isVerificationCodeSent = true;

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

<style lang="scss">
@use "@/styles/pages/auth.scss";
.custom-file-input {
  width: 100%;
}

.profile-picture {
  border: 2px solid #000;
  width: 100px;
  height: 150px;
}
</style>
