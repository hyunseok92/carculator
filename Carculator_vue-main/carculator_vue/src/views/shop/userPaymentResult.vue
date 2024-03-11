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
const billDetail = ref([])
const bill = ref([])
const memberCode = sessionStorage.getItem("memberCode")
onMounted(() => {
  axios.get('http://localhost:8888/shop/paymentUser/' + memberCode).then(response => {
    bill.value = response.data
    console.log(bill.value);
  })
})
const billDetailView = (billNo) => {
  console.log("주문번호: ", billNo);
  
  axios.get(`http://localhost:8888/shop/paymentResult/${billNo}`)
    .then(response => {
    billDetail.value = response.data;
    console.log(billDetail.value);
        
    getlist();
    })
    .catch(error => {
      console.error(error);
    });
};

const cancelUpdate = (billNo, billCancle) => {
  console.log("취소요청: ", billCancle);
  console.log("주문번호: ", billNo);
  axios.put(`http://localhost:8888/shop/paymentUser/${billNo}`, { billNo: billNo, billCancle: billCancle })
    .then(response => {
      
      alert('취소 요청이 등록되었습니다.');
      getlist();
    })
    .catch(error => {
      console.error(error);
    });
};
const delOkUpdate = (billNo) => {
  console.log("주문번호: ", billNo);
  axios.put(`http://localhost:8888/shop/paymentUserDeliOk/${billNo}`)
    .then(response => {
      alert('배송 확인 감사합니다.');
      getlist();
    })
    .catch(error => {
      console.error(error);
    });
};
const getlist = () => {
  axios.get(`http://localhost:8888/shop/paymentUser/${memberCode}`)
    .then(response => {
      bill.value = response.data;
      console.log(bill.value);
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
 if (status === '취소 처리중')
    return 'warning'
  if (status === '취소완료')
    return 'error'

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
                v-if="item.raw.billCancle == null || item.raw.billNow === '취소완료'"           
            > 
              {{ item.columns.billNow }}
            </VChip>
            <VChip
              size="small"
              label
              v-if="item.raw.billCancle != null && item.raw.billNow !== '취소완료'"
              :color="productStatusColor('취소 처리중')"
            >
              취소 처리중
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
                                v-model="billCancle"
                                placeholder="취소 사유를 입력해 주세요"
                                :rules="[v => !!v || '취소 사유를 입력해 주세요.']"
                                label="취소 사유"
                                v-if="item.raw.billCancle == null"
                            />
                            <VTextField
                                v-model="item.raw.billCancle"
                                label="취소 사유"
                                v-else
                                disabled
                            />
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
                        text="취소 요청"
                        @click="cancelUpdate(item.raw.billNo, billCancle)"
                        v-if="item.raw.billCancle == null"
                        ></v-btn>
                        <v-btn
                        text="배송 확인"
                        @click="delOkUpdate(item.raw.billNo)"
                        v-if="item.raw.billCancle == null && item.raw.billNow === '배송중'"
                        ></v-btn>

                        <v-btn
                        text="닫기"
                        @click="isActive.value = false"
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

<style lang="scss">
@use "@/styles/libs/swiper";
</style>
