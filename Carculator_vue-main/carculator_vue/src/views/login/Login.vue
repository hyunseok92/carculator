<script setup>
import kakaoImg from "@/assets/member/kakao.png";
import naverImg from "@/assets/member/naver.png";
import authBgDark from "@/assets/pages/auth-bg-dark.svg";
import authBgLight from "@/assets/pages/auth-bg-light.svg";
import authLoginImg from "@/assets/pages/working-desk-with-laptop.png";
import axios from "@axios";
import { useRoute, useRouter } from "vue-router";
import { useTheme } from "vuetify";
import { VForm } from "vuetify/components/VForm";

const loginForm = ref();
const isPasswordVisible = ref(true);
const isContentExpand = ref(true);
const theme = useTheme();

const authBgThemeVariant = computed(() => {
  return theme.current.value.dark ? authBgDark : authBgLight;
});

const router = useRouter();
const route = useRoute();
</script>

<template>
  <div class="auth-wrapper">
    <VCard max-width="900" min-width="350">
      <VRow no-gutters>
        <VCol
          md="6"
          cols="12"
          class="border-e pa-sm-8 pa-4"
          style="height: 37rem"
        >
          <VCardItem>
            <VCardTitle>로그인</VCardTitle>
          </VCardItem>

          <VCardText>
            <VForm ref="loginForm">
              <VExpandTransition>
                <div v-show="isContentExpand">
                  <VTextField
                    v-model="member.userEmail"
                    label="아이디"
                    :rules="[(v) => !!v || '아이디를 입력해주세요']"
                    class="mb-6"
                  />

                  <VTextField
                    v-model="member.userPassword"
                    :type="isPasswordVisible ? 'password' : 'text'"
                    label="비밀번호"
                    :rules="[(v) => !!v || '비밀번호를 입력해주세요']"
                    :append-inner-icon="
                      isPasswordVisible
                        ? 'mdi-eye-off-outline'
                        : 'mdi-eye-outline'
                    "
                    class="mb-6"
                    @click:append-inner="isPasswordVisible = !isPasswordVisible"
                    @keyup.enter="defaultLoginClick"
                  />

                  <VBtn
                    class="mb-6"
                    block
                    color="primary"
                    @click="defaultLoginClick"
                    style="height: 47px"
                  >
                    로그인
                  </VBtn>
                  
                  <!-- <RouterLink :to="{ name: 'facelogin'}">
                  <VBtn
                    class="mb-6"
                    block
                    color="primary"
                    style="height: 47px"
                  >
                  
                   얼굴 로그인
                  
                  </VBtn>
                  </RouterLink> -->

                  <div class="text-center mb-6">
                    <RouterLink :to="{ name: 'forgotpassword' }">
                      비밀번호를 잊으셨나요?
                    </RouterLink>
                  </div>
                </div>

               
              </VExpandTransition>
            </VForm>

            <div v-show="isContentExpand">
              <div class="d-flex align-center my-6">
                <VDivider />
                <span class="mx-3">OR</span>
                <VDivider />
              </div>

              <p class="font-weight-medium text-center">소셜 로그인</p>

              <div class="d-flex justify-center gap-4">
                <VBtn
                  icon
                  variant="text"
                  color="info"
                  size="small"
                  @click="naverClick"
                >
                  <VImg :src="naverImg" width="24" height="24" />
                </VBtn>

                <VBtn
                  icon
                  variant="text"
                  color="info"
                  size="small"
                  @click="kakaoClick"
                >
                  <VImg :src="kakaoImg" width="24" height="24" />
                </VBtn>

                <VBtn icon variant="text" size="small" @click="gitClick">
                  <VIcon size="24" icon="mdi-github" />
                </VBtn>

                <VBtn
                  icon
                  variant="text"
                  color="error"
                  size="small"
                  @click="googleClick"
                >
                  <VIcon size="24" icon="mdi-google" />
                </VBtn>
              </div>
            </div>
          </VCardText>
        </VCol>

        <VCol
          cols="6"
          class="pa-8 text-center d-none d-md-block"
          :style="`background-image:url(${authBgThemeVariant});`"
        >
          <div class="d-flex align-center justify-center">
            <img width="280" :src="authLoginImg" />
          </div>

          <h6 class="text-body-1 font-weight-semibold mb-3">
            아이디가 없으신가요?
          </h6>

          <VCol>
            <VBtn
              :to="{ name: 'registerUser' }"
              variant="outlined"
              color="primary"
              append-icon="mdi-account-plus-outline"
            >
              일반 회원가입
            </VBtn>
          </VCol>
          <VCol>
            <VBtn
              :to="{ name: 'registerShop' }"
              variant="outlined"
              color="primary"
              append-icon="mdi-account-plus-outline"
            >
              업체 회원가입
            </VBtn>
          </VCol>
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
        userEmail: null,
        userPassword: null,
      },
    };
  },
  methods: {
    async defaultLoginClick() {
      try {
        console.log("로그인 발생");

        // axios를 사용하여 서버로 데이터를 전송
        const response = await axios.post(
          "http://localhost:8888/login",
          this.member
        );

          if (response.status == 200) {
        
          console.log("로그인 성공");
          
          sessionStorage.setItem("accessToken", response.headers.access);
          sessionStorage.setItem("refreshToken", response.headers.refresh);

          console.log("받은 토큰 : " + response.headers.access);
          console.log("받은 리프레시 토큰 : " + response.headers.refresh);
          
          sessionStorage.setItem("memberCode", response.data.membercode);
          sessionStorage.setItem("userName", response.data.userName);
          sessionStorage.setItem("userEmail", response.data.userEmail);
          sessionStorage.setItem("userRole", response.data.role)

          this.$router.push({ name: "main" });
        }


      } catch (error) {
        // 오류 처리
        console.error("로그인 버튼 오류:", error);

        if (error.response && error.response.status === 404) {
          // 서버에서 온 에러 응답
          alert("아이디를 찾을 수 없습니다");
        } else if (error.response && error.response.status == 401) {
          alert(error.response.data);
        }
      }
    },
    async naverClick() {
        axios.get("http://localhost:8888/naverLogin")
        .then((res) => {
          console.log('response data : ' + res.data);
          const requrl = res.data;
          location.href = requrl;

        })
        .catch((err) =>  {
          console.error(err)
        });    
    },
    async gitClick() {
      try {
        
        window.location.href="https://github.com/login/oauth/authorize?client_id=Iv1.be23e54ad1a79529";
      
      } catch (error) {
        console.error("Error google login click", error);
      }
    },
    async googleClick() {
      try {

        window.location.href="https://accounts.google.com/o/oauth2/v2/auth?client_id=857445034554-eti63jl7ed0vrtdtnr6a0mu5f6p54ia5.apps.googleusercontent.com&redirect_uri=http://localhost:8888/googleLogin/callback&response_type=code&scope=email profile";
        
      } catch (error) {
        console.error("Error google login click", error);
      }
    },
    async kakaoClick() {
      try {
        window.Kakao.Auth.login({
          success: async (authObj) => {
            const accessToken = authObj.access_token;

            const response = await axios.get(
              "http://localhost:8888/kakaoLogin/" + accessToken
            );

            if (response.status == 200) {
              console.log(response);

              sessionStorage.setItem("accessToken", response.headers.access);
              sessionStorage.setItem("refreshToken", response.headers.refresh);
              sessionStorage.setItem("memberCode", response.data.membercode);
              sessionStorage.setItem("userName", response.data.userName);
              sessionStorage.setItem("userEmail", response.data.userEmail);
              sessionStorage.setItem("userRole", response.data.role);

              this.$router.push({ name: "main" });
            }
          },
          fail: (err) => {
              alert(err.response.data.message);
          },
        });
      } catch (error) {
        console.error("Error kakao login click", error);
      }
    },
  },
};
</script>

<style lang="scss">
@use "@/styles/pages/auth.scss";
</style>