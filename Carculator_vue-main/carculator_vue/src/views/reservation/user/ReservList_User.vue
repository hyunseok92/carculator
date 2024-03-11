<script setup>
import { VDataTable } from 'vuetify/labs/VDataTable'
import { ref, onMounted } from "vue"

const itemsPerPage = ref(5)

const membercode = sessionStorage.getItem("memberCode");

const headers = [
  {
    title: '정비소명',
    align: 'start',
    sortable: false,
    key: 'repairshop_name',
  },
  {
    title: '정비요소',
    align: 'end',
    key: 'repair_item',
  },
  {
    title: '예약일시',
    align: 'end',
    key: 'reserv_date',
  },
  {
    title: '확정여부',
    align: 'end',
    key: 'reserv_yn',
  },
]

const reservations = ref([])

const fetchReservations = async () => {
  
  try {
    const response = await fetch("http://localhost:8888/mReservation/" + membercode)
    // 정비소코드 데이터 불러오는거로 수정필요
    const data = await response.json()

    reservations.value = data

    data.forEach((reservation) => {
      const dateString = reservation.reserv_date;
      const dateObj = new Date(dateString);

      const year = dateObj.getFullYear().toString().slice(-2);
      const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
      const date = dateObj.getDate().toString().padStart(2, '0');
      const hour = dateObj.getHours().toString().padStart(2, '0');
      const minute = dateObj.getMinutes().toString().padStart(2, '0');

      const formattedDate = `${year}.${month}.${date}. ${hour}시`;

      reservation.reserv_date = formattedDate;
    });
    // console.log(reservations.value);
  } catch (error) {
    console.log('Error')
  }
}

onMounted(async () => {
  fetchReservations()
})

</script>

<template>
  <VDataTable
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="reservations"
    item-value="repairshop_code"
    class="text-no-wrap"
    @click:row="moveToDetail"
  >
    <template #item.repairshop_name="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.repairshop_name } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.repairshop_name }}
      </RouterLink>
    </template>

    <template #item.repair_item="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.repair_item } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.repair_item }}
      </RouterLink>
    </template>

    <template #item.car_model="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.car_model } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.car_model }}
      </RouterLink>
    </template>

    <template #item.reserv_date="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.reserv_date } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.reserv_date }}
      </RouterLink>
    </template>

    <template #item.reserv_yn="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.reserv_yn } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.reserv_yn }}
      </RouterLink>
    </template>
    
    <template #item.reserv_code="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.reserv_code } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.reserv_code }}
      </RouterLink>
    </template>
  </VDataTable>
  <div class="buttonsContainer">
    <div class="buttons">
    <VBtn @click="makeReservation" color="success" variant="tonal" style="margin-top:10px;">
      예약하기
    </VBtn>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    moveToDetail(item) {
      const code = item.raw.reserv_code;
      console.log("리스트에서 보낼 때 : " + code)
      this.$router.push({
        path: 'reservDetail_user', 
        query: { code: code }
      });
    },
    makeReservation() {
      this.$router.push("/repsMap")
    },
    mounted() {
      this.$nextTick(() => {
        this.$refs.listItems[0].$el.click();
      })
    }
  }
}

</script>

<style>

</style>