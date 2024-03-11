<template>
  <!-- 결제 성공 시 -->
  <section v-if="confirmed">
    <div class="result wrapper">
    <div class="box_section">  
      <h2 style="padding: 20px 0px 10px 0px">
        <img
          width="35"
          src="https://static.toss.im/3d-emojis/u1F389_apng.png"
        />
        결제 성공
      </h2>
    <p>paymentKey = {{ this.$route.query.paymentKey }}</p>
    <p>orderId = {{ this.$route.query.orderId }}</p>
    <p>amount = {{ this.$route.query.amount }}</p>
    <div class="result wrapper">
        <button class="button" onclick="location.href='https://docs.tosspayments.com/guides/payment-widget/integration';"
        style="margin-top:30px; margin-right: 10px">연동 문서</button>
        <button class="button" onclick="location.href='https://discord.gg/A4fRFXQhRu';"
        style="margin-top:30px;background-color: #e8f3ff;color:#1b64da ">실시간 문의</button>
      </div>   
    </div>
  </div>
  </section>
</template>


<script>
import '@/views/shop/style.css'
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { confirmPayment } from "@/confirmPayment";
import axios from "axios";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const confirmed = ref(false);
 

    onMounted(async () => {
      const requestData = {
        orderId: route.query.orderId,
        amount: route.query.amount,
        paymentKey: route.query.paymentKey,
      };


      async function confirm() {
        try {
          const { response, json } = await confirmPayment(requestData);
          console.log(json.method);

          if (!response.ok) {
            router.push(`/fail?message=${json.message}&code=${json.code}`);
          } else {
            // 결제 성공 정보를 서버에 저장
            confirmed.value = true;
            console.log("DB 입력값 확인 " + JSON.stringify(requestData));

            // confirmed가 true일 때만 서버로 결제 성공 정보 전송
            if (confirmed.value) {
              await axios.patch('http://localhost:8888/shop/success/', {
                billNo: route.query.orderId,
                billAmount: route.query.amount,
                billKind: json.method,
                paymentkey: route.query.paymentKey,
              });
            }
          }
        } catch (error) {
          console.error('에러가 발생하였습니다!! :', error);
        }
      }

      confirm();
    });

    return {
      confirmed,
    };
  },
};
</script>