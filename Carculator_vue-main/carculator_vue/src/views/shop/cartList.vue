<script setup>
import axios from '@axios'
import { onMounted } from 'vue'
import { VDataTable } from 'vuetify/labs/VDataTable'

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
    title: '장바구니',
    disabled: true,
  },
]

const cartL = ref([])

const headers = [
  {
    title: '상품명',
    key: 'productName',
  },
  {
    title: '가격',
    key: 'productAmount',
  },
  {
    title: '구매 수량',
    key: 'billCount',
  },
 {
    title: 'Actions',
    key: 'actions',
    sortable: false,
},
 
]

const searchProduct = ref('')
const memberCode = sessionStorage.getItem("memberCode");
onMounted(() => {
  axios.get('http://localhost:8888/shop/cartList/' + memberCode, {}).then(response => {
    cartL.value = response.data
    console.log(cart.value);
  })
})

 const deleteCart = cartCode => {
   axios.delete('http://localhost:8888/shop/cartList/' + cartCode, {data: {memberCode: memberCode}}).then(response => {
     cartL.value = response.data
     alert('삭제되었습니다.')
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

    <!-- data table and filter -->
    <VCol cols="6">
      <VCard title="Cart">
        <template #append>
          <div style="width: 10rem;">
            <VTextField
              v-model="searchProduct"
              density="compact"
              prepend-inner-icon="mdi-magnify"
              placeholder="Search..."
            />
          </div>
        </template>
        <p style="display:none">{{billAmountOK}}</p>
        <p style="display:none">{{billCounts}}</p>
        <p style="display:none">{{products}}</p>
        <VDataTable
          :search="searchProduct"
          :headers="headers"
          :items="cartL"
          :items-per-page="8"
          class="table-borderless rounded-0 text-no-wrap "
        >
         
          <template #item.productName="{ item }">
            <div class="d-flex align-center gap-2">
               <VCheckbox
                  v-model="products"
                  :value="item.raw"
                  @change="countok()"
                />
              <VAvatar :image="'/productImages/' + item.raw.productRmg" /> 
              <span>{{ item.raw.productName }}</span>
            </div>
          </template>

          <template #item.productAmount="{ item }">
            {{ item.raw.productAmount }}원
          </template>

          <template #item.billCount="{ item }">
         
           <VTextField
              type="number"
              :min="1"
              :max="item.raw.productCount"
              class="text-center"
              v-model="item.raw.billCount"
                
            ></VTextField>  
           
       
          </template> 

           <template #item.actions="{ item }">
            <div>
              <VBtn
                icon
                variant="plain"
                size="x-small"
                @click="deselectAll();deleteCart(item.raw.cartCode)"
              >
                <VIcon
                  size="20"
                  icon="mdi-delete-outline"
                />

                <VTooltip activator="parent">
                  제거
                </VTooltip>
              </VBtn>
            </div>
          </template>
        </VDataTable>
      </VCard>
    </VCol>
    <VCol cols="6">
      <VCard>
        <VCardTitle>결제 정보</VCardTitle>
        <VCardText>
          <VRow>
            <VCol cols="6">
              <VTextField
                v-model="billAmountOK"
                label="총 결제 금액"
                suffix="원"
                readonly
              />
            </VCol>
            <VCol cols="6">
              <VBtn color="success" @click="movePayment">결제하기</VBtn>
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
      products: ([]),
      billCount: ([]),
      billAmount: '',
        };
    },
  computed: {
    productAmounts() {
    return this.productAmounts = this.products.map(product => product.productAmount);
    },
    billCounts() {
      this.mbillCounts();
      return this.billCount;
    },
    billAmountOK(){
      this.mbillAmountOK();
      return this.billAmount;
    }
  },          
  methods: {
    countok(){
      console.log(this.billCount)
    },
    mbillCounts() {
      this.billCount = this.products.map(product => product.billCount);
    },
    mbillAmountOK(){
      this.billAmount = 0;
      this.productAmounts = this.products.map(product => product.productAmount);
      for (let i = 0; i < this.productAmounts.length; i++) {
        this.billAmount += this.productAmounts[i] * this.billCount[i];
      }
    
    },
    deselectAll() {
      this.products = [];

    },
    movePayment(){
        const productsString = JSON.stringify(this.products);
        this.billCount.push(this.inputValue);
        const billCountString = JSON.stringify(this.billCount);
        for (let i = 0; i < this.products.length; i++) {
          axios.delete('http://localhost:8888/shop/cartList/' + this.products[i].cartCode, {data: {memberCode: sessionStorage.getItem("memberCode")}}).then(response => {
            cartL.value = response.data
            
          })
        }
        this.$router.push({ path: '/shop/product/payment/', query: { products:productsString, billAmount:this.billAmount, billCount: billCountString } })
      },
    
  }

}

</script>

<style lang="scss">
.product-id {
  color: rgba(var(--v-theme-on-surface), var(--v-high-emphasis-opacity));

  &:hover {
    color: rgba(var(--v-theme-primary), var(--v-high-emphasis-opacity));
  }
}
</style>
