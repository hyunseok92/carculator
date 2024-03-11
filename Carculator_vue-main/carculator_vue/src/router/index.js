import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: () => {
    // always scroll to top
    return { top: 0, behavior: 'smooth' }
  },
  routes: [
    {
      path: '/',
      redirect: "/loginForm"
    },
    {
      path: '/facelogin',
      name: "facelogin",
      component: () => import("@/views/login/FaceLogin.vue"),
      meta: {
        layout: "blank",
      },
    },
    { 
      path: "/blank",
      name: "blank",
      component: () => import("@/views/Blank.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: '/admin/memberSelect',
      name: 'memberSelect',
      component: () => import("@/views/admin/memberSelect.vue"),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/loginForm',
      name: 'home',
      component: () => import("@/views/login/Login.vue"),
      meta: {
        layout: 'blank',
      },
    },
    {
      path: "/naverLogin/callback",
      name: 'naverLogin',
      component: () => import("@/views/login/NaverLogin.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: "/git/callback",
      name: 'gitLogin',
      component: () => import("@/views/login/GitLogin.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: "/googleLogin/callback",
      name: 'googleLogin',
      component: () => import("@/views/login/GoogleLogin.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: "/main",
      name: "main",
      component: () => import("@/views/Home.vue"),
      meta: {
        layout: "content",
      },
    },
    {
      path: "/member/memberInfo",
      name: "memberInfo",
      component: () => import("@/views/member/memberInfo.vue"),
      meta: {
        layout: "content",
      },
    },
    {
      path: "/registerUser",
      name: "registerUser",
      component: () => import("@/views/login/RegisterUser.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: "/registerShop",
      name: "registerShop",
      component: () => import("@/views/login/RegisterShop.vue"),
      meta: {
        layout: "blank",
      },
    },
    {
      path: '/forgotpassword',
      name: 'forgotpassword',
      component: () => import('@/views/login/ForgotPassword.vue'),
      meta: {
        layout: 'blank',
      },
    },
    {
      path: '/forgotchangepwd',
      name: 'forgotchangepwd',
      component: () => import('@/views/login/ForgotChangePwd.vue'),
      meta: {
        layout: 'blank',
      },
    },
    {
      path: '/shopAdminInsert',
      name: 'shopAdminInsert',
      component: () => import('@/views/shop/shopAdminInsert.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop',
      name: 'shopList',
      component: () => import('@/views/shop/shopList.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/product',
      name: 'productDetail',
      component: () => import('@/views/shop/productDetail.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shopAdminList',
      name: 'shopAdminList',
      component: () => import('@/views/shop/shopAdminList.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/shopAdminList/update/',
      name: 'shopAdminUpdate',
      component: () => import('@/views/shop/productUpdate.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/product/payment/',
      name: 'payment',
      component: () => import('@/views/shop/shopPayment.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/success/',
      name: 'success',
      component: () => import('@/views/shop/success.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/fail',
      name: 'fail',
      component: () => import('@/views/shop/paymentFail.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/cart',
      name: 'cart',
      component: () => import('@/views/shop/cartList.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/paymentResult',
      name: 'paymentResult',
      component: () => import('@/views/shop/paymentSelect.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/shop/paymentUser',
      name: 'paymentUser',
      component: () => import('@/views/shop/userPaymentResult.vue'),
      meta: {
        layout: 'content',
      },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/NotFound.vue'),
      meta: {
        layout: 'blank',
      },
    },

    // 예약리스트 (업체)
    {
      path: '/reservList_shop',
      name: 'reservList_shop',
      component: () => import('@/views/reservation/repairshop/ReservList_Shop.vue'),
      meta: {
        layout: 'content',
      },
    },
    // 예약리스트 (유저)
    {
      path: '/reservList_user',
      name: 'reservList_user',
      component: () => import('@/views/reservation/user/ReservList_User.vue'),
      meta: {
        layout: 'content',
      },
    },

    // 예약 상세보기 (업체)
    {
      path: '/reservDetail_shop',
      name: 'reservDetail_shop',
      component: () => import('@/views/reservation/repairshop/ReservDetail_Shop.vue'),
      meta: {
        layout: 'content',
      },
    },
    // 예약 상세보기 (유저)
    {
      path: '/reservDetail_user',
      name: 'reservDetail_user',
      component: () => import('@/views/reservation/user/ReservDetail_User.vue'),
      meta: {
        layout: 'content',
      },
    },
    
    // 예약 작성페이지 (유저)
    {
      path: '/reservWrite_user/:repairshop_code',
      name: 'reservWrite_user',
      component: () => import('@/views/reservation/user/ReservWrite_User.vue'),
      meta: {
        layout: 'content',
      },
      props: true,
    },

    // 내 차 정보페이지
    {
      path: '/mycarInfo',
      name: 'mycarInfo',
      component: () => import('@/views/mycar/MyCarInfo.vue'),
      meta: {
        layout: 'content',
      },
    },

    // 내 차 등록
    {
      path: '/mycarWrite',
      name: 'mycarWrite',
      component: () => import('@/views/mycar/MyCarWrite.vue'),
      meta: {
        layout: 'content',
      },
    },

    // 정비소 재고관리페이지
    {
      path: '/shopInventory',
      name: 'shopInventory',
      component: () => import('@/views/rshop_Inventory/Inventory_Shop.vue'),
      meta: {
        layout: 'content',
      },
    },

    // 비밀번호 변경 페이지
    {
      path: '/changePassword',
      name: 'changePassword',
      component: () => import('@/views/mycar/ChangePassword.vue'),
      meta: {
        layout: 'content',
      },
    },

    // 비밀번호 변경 페이지
    {
      path: '/modifyInfo_shop',
      name: 'modifyInfo_shop',
      component: () => import('@/views/mycar/ModifyInfo_shop.vue'),
      meta: {
        layout: 'content',
      },
    },

     // 공지사항
     {
      path: '/notice',
      name: 'notice',
      component: () => import('@/views/notice/notice.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/check-password',
      name: 'check-password',
      component: () => import('@/views/mycar/CheckingPw.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/noticeWrite',
      name: 'noticeWrite1',
      component: () => import('@/views/notice/noticeWrite.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/noticeWrite/:notice_no',
      name: 'noticeWrite2',
      component: () => import('@/views/notice/noticeWrite.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/noticeDetail/:notice_no',
      name: 'noticeDetail',
      component: () => import('@/views/notice/noticeDetail.vue'),
      meta: {
        layout: 'content',
      }
    },

    //현수 작업물
    {
      path: '/damage',
      name: 'Damage',
      component: () => import('@/views/damaged/DmgImg.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/carchat',
      name: 'carchat',
      component: () => import('@/views/chatAI/CarChat.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/repsMap',
      name: 'RepsMap',
      component: () => import('@/views/repairshopMap/RepsMap.vue'),
      meta: {
        layout: 'content',
      }
    },
    {
      path: '/repDetail/:repairshop_code',
      name: 'RepDetail',
      component: () => import('@/views/repairshopMap/RepDetail.vue'),
      meta: {
      layout: 'content',
      },
      props: true,
    }
  ],
})

export default router
