<script setup>
import cover from '@/assets/pages/cover.jpg'
import axios from '@axios'
import { VDataIterator } from 'vuetify/labs/VDataIterator'

const tags = ref([]);

const filters = ref([
  {
    text: '차량 용품',
    value: '차량 용품',
    prependIcon: 'mdi-shape-outline',
  },
  {
    text: '세차 용품',
    value: '세차 용품',
    prependIcon: 'mdi-shape-outline',
  },
  {
    text: '가격 높은 순',
    value: '가격 높은 순',
    prependIcon: 'mdi-trending-up',
  },
  {
    text: '가격 낮은 순',
    value: '가격 낮은 순',
    prependIcon: 'mdi-trending-down',
  },
  {
    text: '조회순',
    value: '조회순',
    prependIcon: 'mdi-fire',
  },
  {
    text: '누적 판매순',
    value: '누적 판매순',
    prependIcon: 'mdi-new-box',
  },
])

const products = ref([])
const search = ref('')
const page = ref(1)
const ItemPerPage = ref(9)

onMounted(() => {
  axios.get('http://localhost:8888/shop/shopAdminList').then(response => {
    products.value = response.data
  })
})

const removeTag = tag => {
  const index = tags.value.findIndex(i => i === tag)

  tags.value.splice(index, 1)
}

const totalPages = computed(() => {
  if (products.value)
    return Math.ceil(products.value.length / ItemPerPage.value)
})

// breadcrumbs
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
]

const filteredProducts = computed(() => {
  let result = [...products.value];  // 상품 배열의 복사본을 만듭니다.

  if (tags.value.includes('가격 높은 순')) {
     // 가격 높은 순과 가격 낮은 순이 모두 선택되어 있다면
      tags.value.splice(tags.value.indexOf('가격 낮은 순'), 1);
      tags.value.splice(tags.value.indexOf('누적 판매순'), 1);
      tags.value.splice(tags.value.indexOf('조회순'), 1);  
    result.sort((a, b) => b.productAmount - a.productAmount);
  } 
  if (tags.value.includes('가격 낮은 순')) {
      // 가격 높은 순과 가격 낮은 순이 모두 선택되어 있다면
      tags.value.splice(tags.value.indexOf('가격 높은 순'), 1);
      tags.value.splice(tags.value.indexOf('누적 판매순'), 1);
      tags.value.splice(tags.value.indexOf('조회순'), 1);  
    result.sort((a, b) => a.productAmount - b.productAmount);
  }
  if (tags.value.includes('조회순')) {
      // 가격 높은 순과 가격 낮은 순이 모두 선택되어 있다면
      tags.value.splice(tags.value.indexOf('누적 판매순'), 1);  
      tags.value.splice(tags.value.indexOf('가격 낮은 순'), 1);
      tags.value.splice(tags.value.indexOf('가격 높은 순'), 1);
    result.sort((a, b) => b.productView - a.productView);
  }
  if (tags.value.includes('누적 판매순')) {
      // 가격 높은 순과 가격 낮은 순이 모두 선택되어 있다면
      tags.value.splice(tags.value.indexOf('조회순'), 1); 
      tags.value.splice(tags.value.indexOf('가격 높은 순'), 1);
      tags.value.splice(tags.value.indexOf('가격 낮은 순'), 1);
    result.sort((a, b) => b.productSum - a.productSum);
  }

  if (tags.value.includes('차량 용품') || tags.value.includes('세차 용품')) {
    result = result.filter(product => tags.value.includes(product.category1));  // 선택된 태그에 상품의 카테고리가 포함되어 있다면 해당 상품을 반환
  }

  return result;
});

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
        <VImg
          :src="cover"
          height="160"
          cover
          gradient="to bottom, rgba(0,0,0,.6), rgba(0,0,0,.5)"
        />
        <VCardText
          class="d-flex flex-column align-center"
          style="margin-top: -7rem;"
        >
          <h4
            class="text-h4 text-white mb-6"
            style="z-index: 2;"
          >
            차량 용품을 검색하세요
          </h4>
          <div class="w-100">
            <VTextField
              v-model="search"
              variant="solo"
              density="default"
              prepend-inner-icon="mdi-magnify"
              placeholder="차량 용품을 입력해주세요"
              bg-color="surface"
              style="max-width: 40rem;"
              class="mx-auto"
            >
              <template #append-inner>
                <VBtn
                  color="info"
                  size="small"
                  elevation="0"
                  icon="mdi-arrow-right"
                />
              </template>
            </VTextField>
          </div>
        </VCardText>
        <VCardText>
          <div class="d-flex justify-space-between gap-4 mt-10">
            <div>
              <VBtn
                color="info"
                prepend-icon="mdi-filter-outline"
              >
                세부 검색
                <VMenu activator="parent">
                  <VList
                    v-model:selected="tags"
                    select-strategy="classic"
                    :items="filters"
                    item-title="text"
                  />
                  
                </VMenu>
                  
              </VBtn>
            </div>
          </div>
          <VDivider />
        </VCardText>
        <VCardText class="d-flex flex-wrap gap-2">
          <VChip
            v-for="tag in tags"
            v-show="filters.filter((f) => f.value === tag).length"
            :key="tag"
            v-bind="filters.filter((f) => f.value === tag)[0]"
            closable
            color="primary"
            close-icon="mdi-close-circle-outline"
            @click:close="removeTag(tag)"  
          />
        </VCardText>
        <VCardText>
          <VDataIterator
            v-if="products && products.length"
            :items="filteredProducts"
            :items-per-page="ItemPerPage"
            :page="page"
            :search="search"
          >
          
            <template #default="{ items }">
              <VRow>
                <VCol
                  v-for="(item, i) in items"
                  :key="i"
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VCard variant="outlined">
                    
                    <VCardText class="bg-background">
                        <router-link :to="{ path: '/shop/product/', query: { productCode: item.raw.productCode } }">
                            <VImg
                                :src="'/productImages/' + item.raw.productRmg"
                                height="200"
                            />
                        </router-link>
                    </VCardText>
                    <VCardText class="d-flex mt-n7 pb-0">
                      <VSpacer />
                      <VBtn
                        icon
                        elevation="10"
                        color="success"
                      >
                        <VIcon
                          size="24"
                          icon="mdi-cart-plus"
                          @click="addCart(item.raw.productCode)"
                        />
                      </VBtn>
                    </VCardText>
                    <VCardText class="mt-4">
                      <div class="d-flex align-center justify-space-between mb-4">
                        <div>
                          <h6 class="text-h6">
                            {{ item.raw.productName }}
                          </h6>
                          
                        </div>
                        <VBtn
                          variant="outlined"
                          color="primary"
                        >
                          {{ item.raw.productAmount }}원
                        </VBtn>
                      </div>
                    </VCardText>
                  </VCard>
                </VCol>
              </VRow>
            </template>
          </VDataIterator>
        </VCardText>
        <VCardText>
          <VPagination
            v-model="page"
            :length="totalPages"
          />
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
</template>
<script>
import axios from '@axios'
export default {
  data(){
    return{
      cart: {
        memberCode: sessionStorage.getItem('memberCode'),
        productCode: '',
      }
    }
  },
  methods: {
    addCart(productCode){
      this.cart.productCode = productCode
      console.log(this.cart)
      axios.post('http://localhost:8888/shop/addCart', this.cart).then(response => {
        
          alert('장바구니에 추가되었습니다.')
        
      })
    }
  }
}
</script>
