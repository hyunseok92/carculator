export default [
    {
        name: 'Home',
        icon: 'mdi-view-dashboard-outline',
        to: { name: 'main' },
    },
    {
        name: '쇼핑몰 관리',
        icon: 'mdi-cart-outline',
        children: [
            { name: '상품 등록', icon: 'mdi-circle', to: { name: 'shopAdminInsert' } },
            { name: '상품 수정', icon: 'mdi-circle', to: { name: 'shopAdminList' } },
            { name: '결제 확인', icon: 'mdi-circle', to: { name: 'paymentResult' } },
            
        ],
    },
    {
        name: '회원관리',
        icon: 'mdi-cube-outline', 
        to: { name: 'memberSelect' },
    },
    {
        name: '공지사항',
        icon: 'mdi-cube-outline',
        to: { name: 'notice' },
    },
]
