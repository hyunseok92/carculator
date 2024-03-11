<script setup>


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
    title: '주문 / 결제',
    disabled: true,
  },
]
</script>
<template>
    <VRow>
        <VCol cols="12">
            <VBreadcrumbs
                :items="breadcrumbs"
                divider="-"
                class="px-0"
            />
        </VCol>
        <VCol cols="6">
            <VCard style="height:320px">
                <VCardTitle>
                    <h4>구매자 정보</h4>
                </VCardTitle>
                <VTable density="compact">
                    <thead>
                    <tr>
                        <th class="text-left">
                        이름
                        </th>
                        <th class="text-left">
                        이메일
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{{bill.payName}}</td>
                        <td>{{bill.billEmail}}</td>
                    </tr>
                    </tbody>
                </VTable>
            </VCard>
        </VCol>
        
        <VCol cols="6">
            <VCard>
                <VCardTitle>
                    <h4>받는 사람 정보</h4>
                </VCardTitle>
                <VTable density="compact">
                    <thead>
                    <tr>
                        <th class="text-left">
                        이름
                        </th>
                        <th class="text-left">
                        연락처
                        </th>
                        <th class="text-left">
                        배송 요청사항
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <VTextField
                                label="이름"
                                v-model="bill.recipient"
                                variant="underlined"
                                :rules="[v => !!v || '받는 사람을 기입해주세요']"
                            />
                        </td>
                        
                        <td>
                            <VTextField
                                label="연락처"
                                v-model="bill.billPhone"
                                variant="underlined"
                                :rules="[v => !!v || '연락처를 기입해주세요', v => /^\d{3}\d{3,4}\d{4}$/.test(v) || '유효한 핸드폰 번호를 입력해주세요']"
                            />
                        </td>
                        <td>
                            <VTextField
                                v-model="bill.billRequest"
                                label="배송요청사항"
                                variant="underlined"
                            />
                        </td>
                        
                    </tr>
                    </tbody>
                </VTable>
                
                <VTable style="margin-top:10px">
                    <thead>
                    <tr>
                        <th class="text-left" colspan="2">
                        배송 주소
                        <VBtn color="secondary"
                        variant="flat" @click="execDaumPostcode">
                        주소 검색
                        </VBtn>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                        <td>
                            <VTextField
                            label="우편번호"
                            prepend-inner-icon="mdi-map-marker"
                            variant="underlined"
                            class="mb-4"
                            v-model="bill.billAddressNo"
                            readonly="readonly"
                            :rules="[v => !!v || '우편 번호를 기입해주세요']"
                            />
                            <VTextField
                            label="도로명주소"
                            prepend-inner-icon="mdi-map-marker"
                            variant="underlined"
                            class="mb-4"
                            v-model="roadAddress"
                            readonly="readonly"
                            />
                        </td>
                        <td>
                            <VTextField
                            label="지번주소"
                            prepend-inner-icon="mdi-map-marker"
                            variant="underlined"
                            class="mb-4"
                            v-model="jibunAddress"
                            readonly="readonly"
                            />
                            <VTextField
                            label="상세주소"
                            v-model="detailAddress"
                            prepend-inner-icon="mdi-map-marker"
                            variant="underlined"
                            class="mb-4"
                            :rules="[v => !!v || '상세 주소를 기입해주세요']"
                            />
                        </td>
                        </tr>
                    </tbody>

                </VTable>
            </VCard>
        </VCol>
        
        <VCol cols="6">
            
                <VCardTitle>
                    <h4>결제 정보</h4>
                </VCardTitle>
                <VCardText>
                    <VTable>
                        <thead>
                            <tr>
                                <th class="text-left">
                                상품명
                                </th>
                                <th class="text-left">
                                수량
                                </th>
                                <th class="text-left">
                                가격
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <tr v-for="(item, i) in products"
                            :key="i">
                                <td>
                                    <VAvatar :image="'/productImages/' + item.productRmg"/> 
                                    {{item.productName}}
                                </td>
                                <td>{{billCount[i]}}개</td>
                                <td>{{item.productAmount * billCount[i]}}원</td>
                            </tr>
                            <tr>
                                <th colspan="2">총 상품 금액</th>
                                <td colspan="2">{{bill.billAmount}}원</td>
                            </tr>
                        </tbody>
                    </VTable>
                </VCardText>
            
        </VCol>
        <VCol cols="6">
            <div class="wrapper">
                <div class="box_section">
                <!-- 결제 UI -->
                <div id="payment-method"></div>
                <!-- 이용약관 UI -->
                <div id="agreement"></div>
                
                <!-- 결제하기 버튼 -->
                <div class="result wrapper">
                    <button @click="requestPayment" class="button" id="payment-button"
                    style="margin-top:30px; ">결제하기</button>
                </div>
                </div>
            </div>
        </VCol>
        
    </VRow>
    
</template>

<script>
import { ANONYMOUS, loadPaymentWidget } from "@tosspayments/payment-widget-sdk";
import '@/views/shop/style.css'
import { nanoid } from 'nanoid';
import axios from 'axios';
export default {
    data() {
        return {
        products: [],
        roadAddress: '',
        jibunAddress: '',
        extraAddress: '',
        detailAddress: '',
        billCount: [],
        paymentWidget: null,
        paymentMethodWidget: null,
        bill: {
            billNo: nanoid(),
            memberCode: sessionStorage.getItem("memberCode"),
            billName: '',
            billAmount: this.$route.query.billAmount,
            recipient: '',
            payName: sessionStorage.getItem("userName"),
            billPhone: '',
            billEmail: sessionStorage.getItem("userEmail"),
            billAddressNo: '',
            billAddress: '',
            billDate: new Date().toISOString(),
            billRequest: '',
        },
        // TODO: clientKey는 개발자센터의 결제위젯 연동 키 > 클라이언트 키로 바꾸세요. 
        clientKey: "test_ck_P9BRQmyarY5LOld1OwW9rJ07KzLN",
        // TODO: customerKey는 구매자와 1:1 관계로 무작위한 고유값을 생성하세요. 
        customerKey: sessionStorage.getItem("memberCode"),
        amount: this.$route.query.billAmount,
        };
    },
    async created() {
        const productsString = this.$route.query.products;
        this.products = JSON.parse(productsString);
        const billCountString = this.$route.query.billCount;
        this.billCount = JSON.parse(billCountString);
        console.log(this.products.length);
        console.log(this.products);
        
        if(this.products.length == 1){
            this.bill.billName = this.products[0].productName;
        }else{
            this.bill.billName = this.products[0].productName + " 외 " + (this.products.length - 1) + "개";
        }
    },
    mounted() {
        this.initializePaymentWidget();
    },
    
    methods: {
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
            this.bill.billAddressNo = data.zonecode;
            this.roadAddress = roadAddr;
            this.jibunAddress = data.jibunAddress;

            // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
            if (roadAddr !== '') {
                this.extraAddress = extraRoadAddr;
            } else {
                this.extraAddress = '';
            }
            },
        }).open();
        },
         async requestPayment() {
            if(this.bill.billAddressNo == "" || this.bill.recipient == "" || this.bill.billPhone == "" || this.bill.detailAddress == ""){
                alert("받는 사람 정보를 입력해주세요");
            }else{
                this.bill.billAddress = this.roadAddress + this.detailAddress;
                console.log(this.bill);
                let dataToSend = {
                    bill: this.bill,
                    products: this.products,
                    billCount: this.billCount,
                };
                axios.post('http://localhost:8888/shop/product/payment/' , dataToSend, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }).then((res) => {
                    
                    console.log(res.data);
                }).catch((err) => {
                    console.log(err)
                });
                
                try {
                    if (this.paymentWidget) {
                    // ------ '결제하기' 버튼 누르면 결제창 띄우기 ------
                    // @docs https://docs.tosspayments.com/reference/widget-sdk#requestpayment결제-정보
                    await this.paymentWidget.requestPayment({
                        orderId: this.bill.billNo,
                        orderName: this.bill.billName,
                        customerName: this.bill.payName,
                        customerEmail: this.bill.billEmail,
                        customerMobilePhone: this.bill.billPhone,
                        successUrl: `http://localhost:8888/success`,
                        failUrl: `${window.location.origin}/fail`
                    });
                    }
                } catch (error) {
                    // 에러 처리하기
                    axios.delete('http://localhost:8888/shop/product/payment/' + this.bill.billNo, {}).then((res) => {
                    console.log(res.data);
                }).catch((err) => {
                    console.log(err)
                })
                    console.error(error);
                }
            }
    },
    
  },
  async mounted() {
    // ------  결제위젯 초기화 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#sdk-설치-및-초기화
    this.paymentWidget = PaymentWidget(this.clientKey, this.customerKey);

    // ------  결제위젯 렌더링 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#renderpaymentmethods선택자-결제-금액-옵션
    this.paymentMethodWidget = this.paymentWidget.renderPaymentMethods(
        "#payment-method",
        { value: this.amount },
        { variantKey: "DEFAULT" }
      );
    // ------  이용약관 렌더링 ------
    // @docs https://docs.tosspayments.com/reference/widget-sdk#renderagreement선택자-옵션
    this.paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });
  },

};

</script>
