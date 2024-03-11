<script setup>
import axios from '@axios';

const description2 = [
 '주문/제작 상품의 경우, 상품의 제작이 이미 진행된 경우',
'상품 포장을 개봉하여 사용 또는 설치 완료되어 상품의 가치가 훼손된 경우 (단, 내용 확인을 위한 포장 개봉의 경우는 예외)',
'고객의 사용, 시간경과, 일부 소비에 의하여 상품의 가치가 현저히 감소한 경우',
'세트상품 일부 사용, 구성품을 분실하였거나 취급 부주의로 인한 파손/고장/오염으로 재판매 불가한 경우',
'모니터 해상도의 차이로 인해 색상이나 이미지가 실제와 달라, 고객이 단순 변심으로 교환/반품을 무료로 요청하는 경우',
'제조사의 사정 (신모델 출시 등) 및 부품 가격 변동 등에 의해 무료 교환/반품으로 요청하는 경우',
]




const products = ref([])
const headers = [
  {
    title: 'Product Name',
    key: 'name',
  },
  {
    title: 'Rating',
    key: 'rating',
  },
  {
    title: 'Price',
    key: 'price',
  },
  {
    title: 'Discount',
    key: 'discount',
  },
]


// breadcrumbs
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '쇼핑몰',
    disabled: true,
  },
  {
    title: '상품 상세페이지',
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

    <VCol cols="12">
      <VCard title="Product Overview">
        <VCardText>
          <VRow>
            <VCol
              cols="12"
              md="5"
            >
              <VImg
                :src="productRmg"
                height="350px"
                class="rounded elevation-3 mx-auto mb-6"
              />
                
              <div class="d-flex flex-wrap gap-4 mt-6" style="justify-content: flex-end;">
                <VTextField
                            v-model="inputValue"
                            type="number"
                            label="구매 수량"
                            :max="productCount"
                            :min="1"
                            
                        ></VTextField>  
                <VBtn color="info"
                @click="addCart(productCode)">
                  장바구니 담기
                  
                </VBtn>

                <VBtn color="success"
                @click="movePayment">
                  바로구매 &gt;
                  
                </VBtn>
              </div>
            </VCol>

            <VCol
              cols="12"
              md="7"
            >
              <div class="ms-md-8">
                <h4 class="text-h5">
                  {{productName}}
                </h4>
                <hr>
                <h6 class="text-h6 mb-3" style="display:flex; justify-content:end;color:red;fontweight:bolder;">
                  {{calculationResult}}원
                </h6>
                      
                <p class="mt-4">
                  Description
                </p>

                <ul class="ms-6">
                  <li
                    class="mb-2"
                  >
                    {{ productDetail }}
                  </li>
                </ul>

                <h6 class="text-h6 mt-6">
                  교환/반품 제한사항
                </h6>
                <VDivider class="my-3" />
                <ul class="mb-0">
                    <li
                    v-for="des2 in description2"
                    :key="des2"
                    class="mb-2"
                    >
                    {{ des2 }}
                    </li> 
                </ul>
              </div>
            </VCol>
            <VCol cols="12">
            <VImg
                :src="detailRmg"
                class="rounded elevation-3 mx-auto mb-6"
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
    return {
      inputValue: "1",
      productCode: this.$route.query.productCode,
      products: ([]),
      productName: '',
      productAmount: '',
      productCount: '',
      category1: "",
      productRmg: "",
      productRmg2: "",
      detailRmg: "",
      detailRmg2: "",
      productDetail: '',
      userCode: '',
      billAmount: '',
      billCount: ([]),
      cart: {
        memberCode: sessionStorage.getItem('memberCode'),
        productCode: '',
        billCount: "",
      }
      
     
        };
    },
  computed: {
    calculationResult() {
      this.billAmount = this.productAmount * this.inputValue;
      return this.billAmount;
    }
  },          
  mounted() {
      console.log(this.productCode);
      this.getProduct();    
  },
  methods: {
      getProduct(){
            axios.get('http://localhost:8888/shop/product/' + this.productCode, {
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
              this.products.push(res.data);
              console.log(this.productName);
          }).catch((err) => {
              console.log(err)
          })
      },
      movePayment(){
        const productsString = JSON.stringify(this.products);
        this.billCount.push(this.inputValue);
        const billCountString = JSON.stringify(this.inputValue);
        this.$router.push({ path: '/shop/product/payment/', query: { products:productsString, billAmount:this.billAmount, billCount: billCountString } })
      },
      addCart(productCode){
      this.cart.productCode = productCode
      this.cart.billCount = this.inputValue
      console.log(this.cart)
      axios.post('http://localhost:8888/shop/addCart', this.cart).then(response => {
        
          alert('장바구니에 추가되었습니다.')
        
      })
    }
  }
}
</script>