<script setup>
import authBgDark from "@/assets/pages/auth-bg-dark.svg";
import authBgLight from "@/assets/pages/auth-bg-light.svg";
import authRegisterImg from "@/assets/pages/working-laptop-while-sitting-chair.png";
import axios from "axios";
import { useTheme } from "vuetify";
import profileDefault from "@/assets/member/profileDefault.png";

const registerForm = ref();
const isPasswordVisible = ref(true);
const isContentExpand = ref(true);
const theme = useTheme();
const checkEmail = ref();

const authBgThemeVariant = computed(() => {
  return theme.current.value.dark ? authBgDark : authBgLight;
});

const selectedImage = ref(null); // 선택된 이미지의 데이터 URL을 저장할 ref

// 이미지를 미리보기하는 함수
function previewImage(event) {
  const file = event.target.files[0];
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader();
    reader.onload = (e) => {
      selectedImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  } else {
    // 파일이 선택되지 않았을 경우, selectedImage를 null로 설정
    selectedImage.value = null;
  }
} 
</script>

<template>
  <div class="auth-wrapper">
    <VCard max-width="2000" min-width="1000">
      <VRow no-gutters>
        <VCol md="6" cols="12" class="border-e pa-sm-8 pa-4">
          <VCardItem>
            <VCardTitle>업체 회원가입</VCardTitle>
          </VCardItem>

          <VCardText class="pb-0">
            <VForm ref="registerForm" @submit.prevent="() => { }">
              <div v-show="isContentExpand">
                <div class="d-flex mb-6">
                  <VTextField v-model="member.email" label="이메일" :rules="[
                    (v) => !!v || '이메일을 입력하세요',
                    (v) =>
                      /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) ||
                      '올바른 이메일 형식을 입력하세요',
                  ]" class="mr-4" />
                  <VBtn color="primary" @click="sendEmailVerification" style="height: 47px">
                    이메일 인증
                  </VBtn>
                </div>

                <div v-show="isVerificationCodeSent" class="row">
                  <div class="d-flex mb-6">
                    <VTextField v-model="member.verificationCode" label="인증 코드" :rules="[(v) => !!v || '인증 코드를 입력하세요']"
                      class="mr-4" />
                    <VBtn color="primary" @click="checkEmailCode" style="height: 47px">
                      인증 확인
                    </VBtn>
                  </div>
                </div>

                <VTextField v-model="member.pwd" :type="isPasswordVisible ? 'password' : 'text'" label="비밀번호" :rules="[
                  (v) => !!v || '비밀번호를 입력하세요',
                  (v) =>
                    /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/.test(
                      v
                    ) ||
                    '영문, 숫자, 특수문자를 조합하여 입력해주세요(8-16자)',
                ]" :append-inner-icon="isPasswordVisible
  ? 'mdi-eye-off-outline'
  : 'mdi-eye-outline'
  " class="mb-6" @click:append-inner="isPasswordVisible = !isPasswordVisible" />

                <VTextField v-model="member.pwd1" :type="isPasswordVisible ? 'password' : 'text'" label="비밀번호 확인" :rules="[
                  (v) => !!v || '비밀번호를 입력하세요',
                  (v) => v === member.pwd || '비밀번호가 일치하지 않습니다.',
                ]" :append-inner-icon="isPasswordVisible
  ? 'mdi-eye-off-outline'
  : 'mdi-eye-outline'
  " class="mb-6" @click:append-inner="isPasswordVisible = !isPasswordVisible" />

                <VTextField v-model="member.name" label="이름" :rules="[(v) => !!v || '이름을 입력하세요']" class="mb-6" />

                <VTextField v-model="member.phone" label="전화번호(ex 010-1234-5678)" :rules="[
                  (v) => !!v || '전화번호를 입력하세요',
                  (v) => /^[0-9-]*$/.test(v) || '숫자와 하이픈(-)만 입력하세요',
                ]" class="mb-6" />

                <VTextField v-model="member.birth" label="생년월일(YYYYMMDD)" :rules="[
                  (v) => !!v || '생년월일을 입력하세요',
                  (v) =>
                    /^[0-9]{8}$/.test(v) || '올바른 생년월일을 입력하세요',
                ]" class="mb-6" />

                <VSelect v-model="member.gender" :items="['남자', '여자']" label="성별" :rules="[(v) => !!v || '성별을 선택하세요']"
                  class="mb-6" />

                <VTextField v-model="member.shopname" label="업체명" :rules="[(v) => !!v || '업체명을 입력하세요']" class="mb-6" />

                <VTextField v-model="member.shopphone" label="업체 전화번호(ex 010-1234-5678)" :rules="[
                  (v) => !!v || '업체 전화번호를 입력하세요',
                  (v) => /^[0-9-]*$/.test(v) || '숫자와 하이픈(-)만 입력하세요',
                ]" class="mb-6" />

                <VTextField v-model="member.shoprate" label="업체 등급" :rules="[
                  (v) => /^[0-9]*$/.test(v) || '숫자만 입력하세요',
                ]" class="mb-6" />

                <VBtn color="primary" variant="flat" @click="execDaumPostcode" class="mb-3">
                  주소 검색
                </VBtn>

                <VTextField label="도로명주소" prepend-inner-icon="mdi-map-marker" variant="underlined" class="mb-4"
                  v-model="member.postcode" readonly="readonly" />


                <VTextField label="상세주소" prepend-inner-icon="mdi-map-marker" variant="underlined" class="mb-4"
                  v-model="member.extraAddress" :rules="[v => !!v || '상세 주소를 기입해주세요']" />

                <div class="row">
                  <div class="d-flex mb-6">
                    <VTextField v-model="member.shopnumber" label="사업자 번호(-없이입력)"
                      :rules="[(v) => !!v || '사업자 번호를 입력하세요', (v) => /^[0-9]*$/.test(v) || '숫자만 입력하세요']" class="mr-4" />
                    <VBtn color="primary" @click="checkShopNumber" style="height: 47px">
                      인증 확인
                    </VBtn>
                  </div>
                </div>

                <VBtn block type="submit" color="primary" @click="submitUserInfo" :disabled="!isAllInputCompleted"
                  style="height: 47px">
                  회원가입
                </VBtn>
              </div>
            </VForm>


          </VCardText>
        </VCol>

        <VCol cols="6" class="pa-8 text-center d-none d-md-block" :style="`background-image:url(${authBgThemeVariant});`">
          <div class="d-flex align-center justify-center">
            <img width="280" :src="authRegisterImg" />
          </div>

          <h6 class="text-body-1 font-weight-semibold mb-3">
            이미 회원이십니까 ?
          </h6>
          <p class="text-body-2">로그인 페이지로 이동합니다</p>
          <VBtn variant="outlined" color="primary" :to="{ name: 'home' }" append-icon="mdi-login">
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
        shopname: null,
        shopphone: null,
        shopnumber: null,
        shopaddress: null,
        shoprate: null,
        postcode: null,
        extraAddress: null,
        grade: "ROLE_SHOP",
        status: 2,
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
    async checkShopNumber() {
      try {
        const data = {
          "b_no": [this.member.shopnumber] // 사업자번호 "xxxxxxx" 로 조회 시,
        };

        // Axios를 사용한 POST 요청
        const response = await axios.post(
          'https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=G2rAkUqPZYwpU7dBh4LPeTzfIjNexBWrlpelfyajDa%2FLd5LvmBI9qvIQ94At0u6epgu7IIWAEaCqBiZ%2BCq7Pww%3D%3D',
          data
        );

        console.log(response.data);
        if (response.status == 200) {

          if (response.data.data[0].b_stt_cd == "01") {
            alert("인증 성공");
          }
          else {
            this.member.shopnumber = "";
            alert("인증 실패");
          }
        }

      } catch (error) {
        console.error("Error checking shopnumber code:", error);
        this.member.shopnumber = "";
        alert("인증 오류");
      }
    },
    execDaumPostcode() {
      new daum.Postcode({
        oncomplete: (data) => {
          // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
          var roadAddr = data.roadAddress; // 도로명 주소 변수
          var extraRoadAddr = ''; // 참고 항목 변수

          // 법정동명이 있을 경우 추가한다. (법정리는 제외)
          // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname;
          }
          // 건물명이 있고, 공동주택일 경우 추가한다.
          if (data.buildingName !== '' && data.apartment === 'Y') {
            extraRoadAddr += extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName;
          }
          // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
          if (extraRoadAddr !== '') {
            extraRoadAddr = ' (' + extraRoadAddr + ')';
          }

          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          this.member.postcode = roadAddr;

          // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
          if (roadAddr !== '') {
            this.member.extraAddress = extraRoadAddr;
          } else {
            this.member.extraAddress = '';
          }

          this.member.shopaddress = this.member.postcode + this.member.extraAddress
        },
      }).open();
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
  width: 200px; // 고정 너비 설정
  height: 200px; // 고정 높이 설정
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden; // 컨테이너를 넘어가는 이미지 숨김

  img {
    width: 100%;
    height: 100%;
    object-fit: cover; // 이미지가 컨테이너를 꽉 채우도록 조정
  }
}
</style>
