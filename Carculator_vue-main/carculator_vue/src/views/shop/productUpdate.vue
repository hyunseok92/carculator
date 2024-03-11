<script setup>
import tableProduct from '@/assets/product/online-school-equipment-home-2.jpg'
import FloatingCard from '@/components/FloatingCard.vue'
import { onMounted } from 'vue'
import axios from '@axios'

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
    title: '상품 목록',
    disabled: true,
  },
   {
    title: '상품 수정',
    disabled: true,
  },
]

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

    <VCol
      cols="12"
      md="4"
    >
      <FloatingCard
        :image="{ src: productRmg }"
        alt = productRmg
      >
      </FloatingCard>
    </VCol>

    <VCol
      cols="12"
      md="8"
    >
      <VCard title="Product Information">
        <VCardText>
          <VForm @submit.prevent="updateProduct">
            <VRow>
              <VCol
                cols="12"
              >
                <VTextField
                  v-model="productName"
                  placeholder="상품명"
                  label="상품명"
                />
              </VCol>

              <VCol
                cols="12"
                sm="6"
                md="4"
              >
                <VTextField
                  v-model="productAmount"
                  label="가격"
                  placeholder="가격"
                />
              </VCol>

              <VCol
                cols="12"
                sm="6"
                md="4"
              >
                <VTextField
                  v-model="productCount"
                  label="수량"
                  placeholder="수량"
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
                  :items="['세차 용품', '차량 용품']"
                />
              </VCol>
              <VCol
                cols="12"
                sm="6"
                md="6"
              >
              <VFileInput
                v-model="primg2"
                prepend-icon="mdi-image-outline"
                ref="fileInputRef2"
                accept="image/png, image/jpeg, image/bmp"
                label="상품 이미지"
                @change="handleFileUpload"
                />
              </VCol>
              <VCol cols="12" md="6">
                <v-file-input
                  v-model="deimg2"
                  ref="fileInputRef"
                  prepend-icon="mdi-image-outline"
                  accept="image/png, image/jpeg, image/bmp"
                  label="상품 상세 이미지"
                  @change="handleFileUpload2"
                  />
                  </VCol>

              <VCol cols="12">
                <VTextarea
                  v-model="productDetail"
                  placeholder="상세설명"
                  label="상세설명"
                  rows="3"
                />
              </VCol>

              <VCol cols="12">
                <VBtn
                  type="submit"
                  color="success"
                  class="me-4"
                >
                  상품 수정
                </VBtn>

                <VBtn
                  variant="outlined"
                  color="error"
                  @click="getProduct()"
                >
                  수정 취소
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>

    <VCol
      cols="12"
      
    >
      <VCard title="상세 이미지">
        <VCardText>
            <VRow>
              <VCol cols="12">
                <FloatingCard
                    :image="{ src: detailRmg }"
                    alt = detailRmg
                />
                </VCol>
              
            </VRow>
        </VCardText>
      </VCard>
    </VCol>

   
      
        
    
  </VRow>
</template>
<script>

export default {
    data() {
        return{
            requestBody: this.$route.query,
            productCode: this.$route.query.productCode,
            productName: '',
            productAmount: '',
            productCount: '',
            category1: "",
            primg: null,
            primg2: null,
            deimg: null,
            deimg2: null,
            productRmg: "",
            productRmg2: "",
            detailRmg: "",
            detailRmg2: "",
            productDetail: '',
        };
    },
    mounted() {
        console.log(this.productCode);
        this.getProduct();
        
    },
    methods: {
        getProduct(){
              axios.get('http://localhost:8888/shop/shopAdminList/update/' + this.productCode, {params: this.requestBody
              }).then((res) => {
                this.productCode = res.data.productCode,
                this.productName = res.data.productName,
                this.productAmount = res.data.productAmount,
                this.productCount = res.data.productCount,
                this.category1 = res.data.category1,
                this.productRmg = '/productImages/' + res.data.productRmg,
                this.productRmg2 = '/productImages/' + res.data.productRmg,
                this.detailRmg = '/productImages/' + res.data.detailRmg,
                this.detailRmg2 = '/productImages/' + res.data.detailRmg,
                this.productDetail = res.data.productDetail,
                console.log(productCode);
            }).catch((err) => {
                console.log(err)
            })
        },
        handleFileUpload() {
            const fileInput = this.$refs.fileInputRef2;

            // 확인을 위한 로그
            console.log(fileInput);

            this.primg = fileInput ? fileInput.files[0] : null;
            console.log(this.primg);
            if (this.primg && this.primg.size > 0) {
                const reader = new FileReader();
                reader.onload = e => {
                this.productRmg = e.target.result;
                };
                reader.readAsDataURL(this.primg);
            } else {
                console.log(this.productRmg2);
                this.productRmg = this.productRmg2;
            } 
        },
        handleFileUpload2() {
            const fileInput = this.$refs.fileInputRef;

            // 확인을 위한 로그
            console.log(fileInput);

            this.deimg = fileInput ? fileInput.files[0] : null;
            console.log(this.deimg);
            if (this.deimg && this.deimg.size > 0) {
                const reader = new FileReader();
                reader.onload = e => {
                this.detailRmg = e.target.result;
                };
                reader.readAsDataURL(this.deimg);
                console.log(this.detailRmg);
            } else {
                console.log(this.detailRmg2);
                this.detailRmg = this.detailRmg2;
            } 
        },
        updateProduct() {
            const formData = new FormData();
            formData.append('pimg', this.primg);
            formData.append('dimg', this.deimg);
            formData.append('productAmount', this.productAmount)
            formData.append('productCount', this.productCount)
            formData.append('category1', this.category1)
            formData.append('productDetail', this.productDetail)
            formData.append('productName', this.productName)
            formData.append('productCode', this.productCode)
            axios.patch('http://localhost:8888/shop/shopAdminList/update/' + this.productCode, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then((res) => {
                console.log('전송 완료');
                this.$router.push({
                    path: '/shopAdminList/'
                })
            }).catch((err) => {
                console.log(err)
            })
        }
    }
}
</script>

