<script setup>
import { toast } from 'vue3-toastify'
import {
  useRtl,
  useTheme,
} from 'vuetify'
import { VForm } from 'vuetify/components/VForm'
import {
  VStepper,
  VStepperHeader,
  VStepperItem,
  VStepperWindow,
  VStepperWindowItem,
} from 'vuetify/labs/VStepper'

const vuetifyTheme = useTheme()
const isRtl = useRtl()

const productData = ref({
  productName: '',
  productAmount: '',
  productCount: '',
  category1: null,
  productDetail: '',
})

const productImages = ref({
  productCount: [],
  dimg: [],
})

// breadcrumbs
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '쇼핑몰 관리',
    disabled: true,
  },
  {
    title: '상품 등록',
    disabled: true,
  },
]

const currentStep = ref(0)
const isInfoStepValid = ref(true)

const refProductInfoForm = ref()


const validateProductInfoForm = () => {
  refProductInfoForm.value?.validate().then(valid => {
    if (valid.valid) {
      currentStep.value++
      isInfoStepValid.value = true
    }
    else {
      isInfoStepValid.value = false
    }
  })
}

</script>

<template>
  <VRow>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs
        :items="breadcrumbs"
        divider="-"
        class="px-0"
      />
    </VCol>

    <VCol cols="12">
      <h4 class="text-h5 font-weight-medium text-center mb-2">
        Add New Product
      </h4>
      <p class="text-lg text-disabled text-center mb-6">
        상품을 등록해 주세요.
      </p>

      <VStepper v-model="currentStep">
        <VStepperHeader>
          <VStepperItem
            title="상품 정보"
            color="primary"
            icon="mdi-help"
            :error="!isInfoStepValid"
            :complete="currentStep === 1"
          />

          <VDivider />

          <VStepperItem
            title="상품 이미지"
            color="primary"
            icon="mdi-image"
            :complete="currentStep === 2"
          />

          

        </VStepperHeader>

        <VStepperWindow
          v-model="currentStep"
          :touch="false"
        >
          <VStepperWindowItem>
            <VForm
              ref="refProductInfoForm"
              @submit.prevent="validateProductInfoForm"
            >
              <VRow>
                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="productName"
                    placeholder="상품 이름"
                    :rules="[v => !!v || '상품 이름을 기입해주세요.']"
                    label="상품 이름"
                  />
                </VCol>

                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="productAmount"
                    label="상품 가격"
                    type="number"
                    :rules="[v => !!v || '상품 가격을 기입해주세요.']"
                    placeholder="상품 가격"
                  />
                </VCol>

                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="productCount"
                    type="number"
                    label="상품 수량"
                    :rules="[v => !!v || '상품 수량을 기입해주세요.']"
                    placeholder="상품 수량"
                  />
                </VCol>

                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VSelect
                    v-model="category1"
                    label="카테고리"
                    placeholder="카테고리"
                    :rules="[v => !!v || '카테고리를 선택해주세요.']"
                    :items="['세차 용품', '차량 용품']"
                  />
                </VCol>

                <VCol cols="12">
                  <VTextarea
                    v-model="productDetail"
                    placeholder="상품 상세 설명"
                    label="상품 상세 설명"
                    :rules="[v => !!v || '상품 상세 설명을 기입해주세요']"
                    rows="3"
                  />
                </VCol>

                <VCol cols="12">
                  <div class="d-flex flex-wrap gap-4 justify-sm-space-between justify-center">
                    <VBtn
                      color="default"
                      disabled
                    >
                      이전
                    </VBtn>

                    <VBtn
                      color="primary"
                      type="submit"
                    >
                      다음
                    </VBtn>
                  </div>
                </VCol>
              </VRow>
            </VForm>
          </VStepperWindowItem>

          <VStepperWindowItem>
            <VForm>
              <VRow>
                <VCol cols="12">
                  
                  <VFileInput
                    v-model="pimg"
                    prepend-icon="mdi-image-outline"
                    accept="image/png, image/jpeg, image/bmp"
                    label="상품 이미지"
                    @change="handleFileUpload"
                  />
                </VCol>

                <VCol cols="12">
                  <VFileInput
                    v-model="dimg"
                    prepend-icon="mdi-image-size-select-large"
                    accept="image/png, image/jpeg, image/bmp"
                    label="상품 상세 설명 이미지"
                    @change="handleFileUpload2"
                  />
                </VCol>

                <VCol cols="12">
                  <div class="d-flex flex-wrap gap-4 justify-sm-space-between justify-center">
                    <VBtn
                      color="secondary"
                      @click="currentStep--"
                    >
                      이전
                    </VBtn>

                    <VBtn
                      color="success"
                      
                      @click="submitData"
                    >
                      등록
                    </VBtn>
                  </div>
                </VCol>
              </VRow>
            </VForm>
          </VStepperWindowItem>
        </VStepperWindow>
      </VStepper>
    </VCol>
  </VRow>
</template>
<script>
import { defineComponent } from 'vue';
import axios from 'axios'

import { createRouter, createWebHistory } from 'vue-router'
import shopAdminListVue from './shopAdminList.vue'
//import instance from './your-instance-file';

const config  = {
  baseURL: 'http://localhost:8888', // 백엔드 서버 URL
  headers: {
    'X-CSRF-TOKEN': this.csrfToken,
  }
};



export default {
  data() {
    return {
      productName: '',
      productAmount: '',
      productCount: '',
      category1: "",
      primg: null,
      deimg: null,
      productDetail: '',
      csrfToken: '',
    };
  },
   mounted() {
    this.getCsrfToken();
  },
  methods: {
     async getCsrfToken() {
      // CSRF 토큰을 서버에서 가져옵니다.
      const response = await axios.get('/csrf-token-endpoint');
      this.csrfToken = response.data.csrfToken;
    },
    handleFileUpload(event) {
      console.log(event.target.files[0]);
      this.primg = event.target.files[0];
    },
    handleFileUpload2(event) {
      
      this.deimg = event.target.files[0];
    },
    submitData() {
      const formData = new FormData();
      formData.append('pimg', this.primg);
      formData.append('dimg', this.deimg);
      formData.append('productAmount', this.productAmount)
      formData.append('productCount', this.productCount)
      formData.append('category1', this.category1)
      formData.append('productDetail', this.productDetail)
      formData.append('productName', this.productName)
      try{
        axios.post('/shop/shopAdminInsert', formData, config)
          .then(response => {
            console.log('전송 완료');
            
            let resolvedRoute = this.$router.resolve({ path: '/shop/shopAdminList', query: this.requestBody });

            
              this.$router.push({
                name: 'shopAdminList'
              });
            
            })
            .catch((error) => {
              console.log(error);
            });
            // 성공적으로 전송되었을 때의 동작을 수행
        
      } catch (error) {
        console.log(error);
      }
    },
          getCsrfToken() {
      // CSRF 토큰을 서버에서 가져오는 비동기 함수를 구현합니다.
      return new Promise((resolve, reject) => {
        axios
          .get('/csrf-token-endpoint')
          .then((response) => {
            resolve(response.data.csrfToken);
          })
          .catch((error) => {
            reject(error);
          });
      });
    
    }
  }
};
</script>





<style lang="scss">
@import "vue3-toastify/dist/index.css";
@import "@/styles/libs/toastify";
</style>
