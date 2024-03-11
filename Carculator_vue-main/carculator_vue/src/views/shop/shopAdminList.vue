<script setup>
import axios from '@axios'
import { onMounted } from 'vue'
import { VDataTable } from 'vuetify/labs/VDataTable'

const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '쇼핑몰 관리',
    disabled: true,
  },
  {
    title: '상품 목록',
    disabled: true,
  },
]

const products = ref([])

const headers = [
  {
    title: '상품명',
    key: 'productName',
  },
  {
    title: '가격',
    key: 'productAmount',
  },
  {
    title: '현 재고 수량',
    key: 'productCount',
  },
  {
    title: '누적 판매 수량',
    key: 'productSum',
  },
 {
   title: 'Actions',
   key: 'actions',
   sortable: false,
 },
]

const searchProduct = ref('')

onMounted(() => {
  axios.get('http://localhost:8888/shop/shopAdminList').then(response => {
    products.value = response.data
    console.log(products.value);
  })
})

 const deleteProduct = productCode => {
   axios.delete('http://localhost:8888/shop/shopAdminList/' + productCode, {}).then(response => {
     products.value = response.data
     alert('삭제되었습니다.')
  
   })
 }
  
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

    <!-- data table and filter -->
    <VCol cols="12">
      <VCard title="Product">
        <template #append>
          <div style="width: 10rem;">
            <VTextField
              v-model="searchProduct"
              density="compact"
              prepend-inner-icon="mdi-magnify"
              placeholder="Search..."
            />
          </div>
        </template>

        <VDataTable
          :search="searchProduct"
        
          :headers="headers"
          :items="products"
          :items-per-page="8"
          class="table-borderless rounded-0 text-no-wrap "
        >
          <!-- <template>
            <RouterLink
              
              class="product-id"
            >
              {{ category1 }}
            </RouterLink>
          </template> -->


             
          <template #item.productName="{ item }">
            <div class="d-flex align-center gap-2">
              <VAvatar :image="'/productImages/' + item.raw.productRmg" /> 
              <span>{{ item.raw.productName }}</span>
            </div>
          </template>

          <template #item.productAmount="{ item }">
            {{ item.raw.productAmount }}원
          </template>

          <template #item.productCount="{ item }">
            <VChip
              label
              color="primary"
              density="comfortable"
              class="text-capitalize"
            >
              {{ item.raw.productCount }}개
            </VChip>
          </template> 
          <template #item.productSum="{ item }">
            {{ item.raw.productSum}}개
          </template>

           <template #item.actions="{ item }">
            <div>

              <VBtn
                icon
                variant="plain"
                size="x-small"
                @click="updateProduct(item.raw.productCode)"
              >
                <VIcon
                  size="20"
                  icon="mdi-pencil-outline"
                />

                <VTooltip activator="parent">
                  상품 수정
                </VTooltip>
              </VBtn>

              <VBtn
                icon
                variant="plain"
                size="x-small"
                @click="deleteProduct(item.raw.productCode)"
              >
                <VIcon
                  size="20"
                  icon="mdi-delete-outline"
                />

                <VTooltip activator="parent">
                  상품 삭제
                </VTooltip>
              </VBtn>
            </div>
          </template>
                

        </VDataTable>
      </VCard>
    </VCol>
  </VRow> 
</template>
<script>
export default {
    methods: {
      updateProduct(productCode) {
        this.$router.push({
          path: '/shop/shopAdminList/update/', 
          query: { productCode: productCode }
        });
      }
    }
}

</script>

<style lang="scss">
.product-id {
  color: rgba(var(--v-theme-on-surface), var(--v-high-emphasis-opacity));

  &:hover {
    color: rgba(var(--v-theme-primary), var(--v-high-emphasis-opacity));
  }
}
</style>
