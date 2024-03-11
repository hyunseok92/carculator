<script setup>
import profileDefault from '@/assets/member/profileDefault.png';
import { useRouter } from 'vue-router';

const router = useRouter()
const userName = sessionStorage.getItem("userName");
const userRole = sessionStorage.getItem("userRole");

const userMenuItems = [
  { title: '나의 회원 정보', route: 'mycarInfo' },
  { title: '장바구니', route: 'cart' },
  { title: '결제 내역', route: 'paymentUser' },
  { title: '정비소 예약', route: '/repair-reservation' },
];

const adminMenuItems = [
  { title: '회원관리', route: 'memberSelect'},
  { title: '공지사항', route: 'notice'}

];


const menuItems = computed(() => {
  if (userRole === 'ROLE_ADMIN') {
    return adminMenuItems;
  } else {
    return userMenuItems;
  }
});

function handleItemClick(item) {
  if (item.route) {
    router.push({ name: item.route });
  } else {
    // 클릭한 항목에 대한 다른 동작 처리
    console.log('Clicked item:', item.title);
  }
}

const logOut = () => {

  sessionStorage.removeItem('accessToken')
  sessionStorage.removeItem('refreshToken')
  sessionStorage.removeItem('memberCode')
  sessionStorage.removeItem('userName')
  sessionStorage.removeItem('userEmail')
  sessionStorage.removeItem('userRole')

  router.replace({ name: 'home' })
}

</script>

<template>
  <VAvatar class="cursor-pointer">
    <VImg :src="profileDefault" />

    <VMenu activator="parent">
      <VList>
          <VListItemTitle class="text-center">{{ userName }}님</VListItemTitle>
          <!-- <VListItemSubtitle>{{ user.email }}</VListItemSubtitle> -->
        <VDivider class="mt-2" />
        <VListItem v-for="item in menuItems" :key="item.title" @click="handleItemClick(item)">
          <VListItemTitle>{{ item.title }}</VListItemTitle>
        </VListItem>
        <VDivider />
        <VListItem @click="logOut">
          <VListItemTitle>Logout</VListItemTitle>
        </VListItem>
      </VList>
    </VMenu>
  </VAvatar>
</template>