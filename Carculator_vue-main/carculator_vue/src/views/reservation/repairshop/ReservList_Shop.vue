<script setup>
import { onMounted, ref } from "vue";
import { VDataTable } from 'vuetify/labs/VDataTable';
import axios from "axios"


const itemsPerPage = ref(5)

let repairshop_code = 0;

const headers = [
  {
    title: '예약자명',
    align: 'start',
    sortable: false,
    key: 'name',
  },
  {
    title: '정비요소',
    align: 'end',
    key: 'repair_item',
  },
  {
    title: '차종',
    align: 'end',
    key: 'car_model',
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
  console.log("fetchReservations 진입 repairshop_code : " + repairshop_code);
  try {
    const response = await fetch("http://localhost:8888/reservations/" + repairshop_code)
    // 정비소코드 데이터 불러오는거로 수정필요
    const data = await response.json();
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
  } catch (error) {
    console.log(error);
    console.log("catch로 빠짐");
  }
}

const getShopCode = async () => {
  const membercode = sessionStorage.getItem("memberCode");
  console.log("getShopCode에서 membercode : " + membercode);
  try {
    const res = await axios.get("http://localhost:8888/reservation/uInfo/" + membercode);
    const data = res.data;
    console.log("repairshop_code : " + data.repairshop_code);
    repairshop_code = data.repairshop_code;
    console.log("axios.get으로 받아온 repairshop_code : " + repairshop_code);
    console.log("axios.get으로 받아온 name : " + data.name);
    fetchReservations();
  } catch (error) {
    console.error(error);
  }
}

onMounted(async () => {
  getShopCode()
  // fetchReservations()
})

</script>

<template>
  <VDataTable
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="reservations"
    item-value="name"
    class="text-no-wrap"
    @click:row="moveToDetail"
  >
    <template #item.name="{ item }">
      <RouterLink
        :to="{ name: 'reservDetail_shop', params: { code: item.raw.name } }"
        class="reservation-id"
        @click="moveToDetail(item)"
      >
        {{ item.raw.name }}
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
</template>

<script>
export default {
    methods: {
      moveToDetail(item) {
        const code = item.raw.reserv_code;
        this.$router.push({
          path: 'reservDetail_shop', 
          query: { code: code }
        });
        console.log(code);
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