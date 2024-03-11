<script setup>
import authBgDark from '@/assets/pages/auth-bg-dark.svg'
import authBgLight from '@/assets/pages/auth-bg-light.svg'
import authForgotPasswordImg from '@/assets/pages/girl-forgot-something.png'
import { useTheme } from 'vuetify'
import { VForm } from 'vuetify/components/VForm'
import axios from "axios";
import {  ref } from "vue"

const forgetPasswordForm = ref();
const theme = useTheme();
const isPasswordVisible = ref(true);
const authBgThemeVariant = computed(() => {
    return theme.current.value.dark ? authBgDark : authBgLight
})
</script>

<template>
    <div class="auth-wrapper">
        <VCard max-width="900" min-width="350" class="fixed-card-size">
            <VRow no-gutters>
                <VCol md="6" cols="12" class="border-e pa-sm-8 pa-4">
                    <VCardItem>
                        <VCardTitle>비밀번호 재설정</VCardTitle>
                    </VCardItem>

                    <VCardText>
                        <VForm ref="forgetPasswordForm">
                            <VTextField v-model="member.pwd" :type="isPasswordVisible ? 'password' : 'text'" label="비밀번호"
                                :rules="[
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

                            <VTextField v-model="member.pwd1" :type="isPasswordVisible ? 'password' : 'text'"
                                label="비밀번호 확인" :rules="[
                                    (v) => !!v || '비밀번호를 입력하세요',
                                    (v) => v === member.pwd || '비밀번호가 일치하지 않습니다.',
                                ]" :append-inner-icon="isPasswordVisible
        ? 'mdi-eye-off-outline'
        : 'mdi-eye-outline'
    " class="mb-6" @click:append-inner="isPasswordVisible = !isPasswordVisible" />

                            <VBtn class="mb-6" block color="primary" @click="ChangePwdClick" style="height: 47px">
                                변경하기
                            </VBtn>

                            <VCol cols="12">
                                <VBtn block variant="text" color="secondary" size="large"
                                    prepend-icon="mdi-chevron-double-left" :to="{ name: 'home' }">
                                    Back to login
                                </VBtn>
                            </VCol>
                        </VForm>
                    </VCardText>
                </VCol>

                <VCol cols="6" class="pa-8 text-center d-none d-md-block"
                    :style="`background-image:url(${authBgThemeVariant});`">
                    <div class="d-flex align-center justify-center">
                        <img width="280" :src="authForgotPasswordImg">
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
                pwd: null,
                pwd1: null,
            },

            isVerificationCodeSent: false, //이메일 인증 코드 전송 여부
            isAllInputCompleted: false, //모든 입력이 완료되었는지 여부
        };
    },
    methods: {        
        async ChangePwdClick() {
            try {

                const urlParams = new URLSearchParams(window.location.search);
                this.member.email = urlParams.get("email");

                console.log("시발아진짜");
                console.log("클릭시" + this.member.email);
                if(this.member.pwd == this.member.pwd1) {
                  const response =  await axios.post("http://localhost:8888/changePwd", this.member);
                  
                  if(response.status == 200) {
                    alert("비밀번호 재설정 완료");

                    

                    this.$router.push({name : "home"});
                  }
                }

            } catch (error) {
                console.log(error);
            }
        }
    },
};
</script>

