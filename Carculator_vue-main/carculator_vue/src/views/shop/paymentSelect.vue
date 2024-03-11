<script setup>
import axios from '@axios'
import { register } from 'swiper/element/bundle'
import { VDataTable } from 'vuetify/labs/VDataTable'


const tableHeader = [
 
  {
    title: '결제 날짜',
    key: 'billDate',
  },
  {
    title: '결제자',
    key: 'payName',
  },
  {
    title: '결제 ID',
    key: 'billEmail',
  },
  {
    title: '주문 상품',
    key: 'billName',
  },
  {
    title: '결제 금액',
    key: 'billAmount',
  },
  {
    title: '받는 사람',
    key: 'recipient',
  },
  {
    title: '핸드폰 번호',
    key: 'billPhone',
  },
  {
    title: '우편 번호',
    key: 'billAddressNo',
  },
  {
    title: '상세 주소',
    key: 'billAddress',
  },
   {
    title: '요청 사항',
    key: 'billRequest',
  },
  {
    title: '결제 수단',
    key: 'billKind',
  },
  {
    title: '진행 사항',
    key: 'billNow',
  },
  {
    title: 'Actions',
    key: 'actions',
  },
]

const bill = ref([])

onMounted(() => {
  axios.get('http://localhost:8888/shop/paymentResult/').then(response => {
    bill.value = response.data
    console.log(bill.value);
  })
})
const getlist = () => {
  axios.get('http://localhost:8888/shop/paymentResult/')
    .then(response => {
      bill.value = response.data
      console.log(bill.value);
    })
    .catch(error => {
      console.error(error);
    });
};
const invNoUpdate = (billNo, invNo) => {
  console.log("송장번호: ", invNo);
  console.log("주문번호: ", billNo);
  axios.put('http://localhost:8888/shop/paymentResult/' + billNo, { billNo: billNo, invNo: invNo })
    .then(response => {
      
      getlist();
      alert('송장번호가 등록되었습니다.');
   
    })
    .catch(error => {
      console.error(error);
    });
};
const cancelUpdate = (billCancle, paymentkey, billNo) => {
  console.log("취소요청: ", billCancle);
  console.log("주문번호: ", billNo);
  console.log("결제키: ", paymentkey);
  const secretKey = "test_sk_ma60RZblrq1JgNWNOb7erwzYWBn1";
  const encryptedSecretKey = btoa(secretKey + ":");
  const options = {
    method: 'POST',
    url: `https://api.tosspayments.com/v1/payments/${paymentkey}/cancel`,
    headers: {
      Authorization: `Basic ${encryptedSecretKey}`,
      'Content-Type': 'application/json'
    },
    data: { cancelReason: billCancle }
  };

  axios(options)
    .then(response => {
      console.log(response.data);
      axios.put('http://localhost:8888/shop/paymentCancel/'+ billNo, {}).then(response => {
      

      alert('주문이 취소되었습니다.');
      getlist();
    })
    .catch(error => {
      console.error(error);
    });
})
    .catch(error => {
      console.error(error);
    });
};

const searchOrder = ref('')

const productStatusColor = status => {
  if (status === '배송완료')
    return 'success'
  if (status === '배송중')
    return 'info'
  if (status === '취소요청')
    return 'error'
  if (status === '결제완료')
    return 'primary'

  return 'secondary'
}



// breadcrumbs
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '상품 관리',
    disabled: true,
  },
  {
    title: '결제 확인',
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
      <VCard>
        <VCardText>
          <VTextField
            v-model="searchOrder"
            prepend-inner-icon="mdi-magnify"
            placeholder="Search Order"
          />
        </VCardText>
      </VCard>
    </VCol>

    <VCol cols="12">
      <VCard>
        <VDataTable
          :headers="tableHeader"
          :search="searchOrder"
          :items="bill"
          :items-per-page="10"
          class="text-no-wrap"
        >
          

          <template #item.billDate="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billDate }}
            </span>
          </template>

          <template #item.payName="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.payName }}
            </span>
          </template>
          <template #item.billEmail="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billEmail }}
            </span>
          </template>
          <template #item.billName="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billName }}
            </span>
          </template>
          <template #item.billAmount="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billAmount }}원
            </span>
          </template>
          <template #item.recipient="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.recipient }}
            </span>
          </template>
          <template #item.billPhone="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billPhone }}
            </span>
          </template>
          <template #item.billAddressNo="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billAddressNo }}
            </span>
          </template>
          <template #item.billAddress="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billAddress }}
            </span>
          </template>

          <template #item.billRequest="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billRequest }}
            </span>
          </template>

          <template #item.billKind="{ item }">
            <span class="font-weight-semibold">
              {{ item.columns.billKind }}
            </span>
          </template>

          <template #item.billNow="{ item }">
            <VChip
              size="small"
              label
              :color="productStatusColor(item.columns.billNow)"
            >
              {{ item.columns.billNow }}
            </VChip>
            <VChip
              size="small"
              label
              :color="productStatusColor('취소요청')"
              v-if="item.raw.billCancle != null && item.raw.billNow !== '취소완료'"
            >
              취소 요청
            </VChip>
          </template>

          <template #item.actions="{ item }">
            
              <v-dialog width="500">
                <template v-slot:activator="{ props }">
                    <VBtn
                        icon
                        variant="plain"
                        size="x-small"
                        v-bind="props"
                        @click="billDetailView(item.raw.billNo)"
                    >
                        <VIcon
                        size="20"
                        icon="mdi-pencil-outline"
                        />

                        <VTooltip activator="parent">
                        상품 상세
                        </VTooltip>
                    </VBtn>
                </template>

                <template v-slot:default="{ isActive }">
                    <v-card title="상품 상세 내역">
                      <VTextField
                                v-model="invNo"
                                placeholder="송장 번호를 입력해 주세요"
                                :rules="[v => !!v || '송장 번호를 입력해 주세요.']"
                                label="송장 번호"
                                v-if="item.raw.invNo == null && item.raw.billCancle == null"
                            />
                      <VAlert color="primary"
                                v-model="item.raw.invNo"
                                v-if="item.raw.invNo != null"
                                
                            >
                                송장 번호 : {{ item.raw.invNo }}
                            </VAlert>
                            <VAlert color="error"
                                v-model="item.raw.billCancle"
                                v-if="item.raw.billCancle != null"
                                
                            >
                                취소 사유 : {{ item.raw.billCancle }}
                            </VAlert>
                      <VTable
                            fixed-header
                            height="300px"
                        >
                            <thead>
                            <tr>
                                <th class="text-left">
                                상품명
                                </th>
                                <th class="text-left">
                                구매 수량
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr
                                v-for="product in billDetail"
                                :key="product"
                            >
                                <td>
                                    {{ product.productName }}
                                </td>
                                <td>{{ product.billCount }}개</td>
                            </tr>
                            </tbody>
                           
                        </VTable>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                        text="송장 등록"
                        @click="invNoUpdate(item.raw.billNo, invNo)"
                        v-if="item.raw.invNo == null && item.raw.billCancle == null"
                        ></v-btn>
                        <v-btn
                        text="취소 승인"
                        @click="cancelUpdate(item.raw.billCancle, item.raw.paymentkey, item.raw.billNo)"
                        v-if="item.raw.billCancle != null && item.raw.billNow !== '취소완료'"
                        ></v-btn>

                        <v-btn
                        text="닫기"
                        @click="isActive.value = false, getlist()"
                        ></v-btn>
                    </v-card-actions>
                    </v-card>
                </template>
                </v-dialog>
            

          </template>
        </VDataTable>
      </VCard>
    </VCol>
  </VRow>
</template>
<script>
import axios from '@axios'
export default {
  data(){
    return{
      billDetail: [],
      billNo: '',
      bill: ([])
    }
  },
  
  
  
  methods: {
    billDetailView(billNo){
        console.log("주문번호 : ",billNo);
        this.billNo = billNo
        axios.get('http://localhost:8888/shop/paymentResult/'+ this.billNo).then(response => {
        this.billDetail = response.data
        console.log(this.billDetail);
      })
    },
    
    // cancelUpdate(billCancle, paymentkey, billNo){
    //   console.log("취소요청 : ",billCancle);
    //   console.log("주문번호 : ",billNo);
    //   console.log("결제키 : ",paymentkey);
    //   const secretKey = "test_sk_ma60RZblrq1JgNWNOb7erwzYWBn1";
    //   const encryptedSecretKey = btoa(secretKey+":")
    //   var options = {
    //     method: 'POST',
    //     url: 'https://api.tosspayments.com/v1/payments/'+ paymentkey +'/cancel',
    //     headers: {
    //       Authorization: `Basic ${encryptedSecretKey}`,
    //       'Content-Type': 'application/json'
    //     },
    //     data: {cancelReason: billCancle}
    //   };

    //   axios.request(options).then(function (response) {
    //     console.log(response.data);
    //     axios.put('http://localhost:8888/shop/paymentCancel/'+ billNo, {}).then(response => {
    //     this.billDetail = response.data
        
    //     alert('취소가 완료되었습니다.')
    //     console.log(this.billDetail);
    //   })
    //   }).catch(function (error) {
    //     console.error(error);
    //   });
      
    }
  }

</script>
<style lang="scss">
@use "@/styles/libs/swiper";
</style>
