export default [
  {
    name: 'Home',
    icon: 'mdi-view-dashboard-outline',
    to: { name: 'main' },
  },
  {
    name: 'AI 차량 파손 견적',
    icon: 'mdi-cube-outline',
    to: { name: 'Damage' },
  },
  {
    name: '쇼핑몰',
    icon: 'mdi-cart-outline',
    to: { name: 'shopList' },
  },
  {
    name: '차량 정비',
    icon: 'mdi-cart-outline',
    children: [

      { name: '나의 예약관리', icon: 'mdi-circle', to: { name: 'reservList_user' } },
      { name: '정비 예약하기', icon: 'mdi-circle', to: { name: 'RepsMap' } },

    ],
  },
  {
    name: '공지사항',
    icon: 'mdi-cube-outline',
    to: { name: 'notice' },
  },
  {
    name: 'AI 채팅',
    icon: 'mdi-keyboard',
    to: { name: 'carchat' },
  },

]