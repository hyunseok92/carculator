<script setup>
import { VDataTable } from 'vuetify/labs/VDataTable'
import axios from "axios"
import { onMounted, ref } from "vue"

const dialog = ref(false)
const dialogDelete = ref(false)

const headers = [
  {
    title: '상품명',
    align: 'start',
    sortable: false,
    key: 'name_product',
  },
  {
    title: '재고수량',
    key: 'quantity',
  },
  {
    title: '가격',
    key: 'price',
  },
  {
    title: 'Actions',
    key: 'actions',
    sortable: false,
  },
]

const products = ref([])
const editedIndex = ref(-1)

const editedItem = ref({
    name_product: '',
    quantity: 0,
    price: 0,
})

const defaultItem = ref({
    name_product: '',
    quantity: 0,
    price: 0,
})

const formTitle = computed(() => {
  return editedIndex.value === -1 ? '새로운 상품 입력' : 'Edit Item'
})

function initialize() {
  products.value = [
    {
      name_product: '요소수',
      quantity: 100,
      price: 10000,
    },
  ]
}
function editItem(item) {
  editedIndex.value = products.value.indexOf(item)
  editedItem.value = Object.assign({}, item)
  dialog.value = true
}
function deleteItem(item) {
  editedIndex.value = products.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialogDelete.value = true
}
// 재고 삭제 axios 추가
function deleteItemConfirm() {
  products.value.splice(editedIndex.value, 1);
  console.log("Item deleted:", editedItem.value)
  closeDelete()
}
function close() {
  dialog.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}
function closeDelete() {
  dialogDelete.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}
// 재고 추가, 수정 axios 추가
async function save() {
  if (editedIndex.value > -1) {
    
    // alert("재고 정보가 수정되었습니다.");
    try {
      const response = await axios.patch(
          "http://localhost:8888/shopInven/", editedItem.value);

      alert("정상적으로 추가되었습니다.");

      Object.assign(products.value[editedIndex.value], editedItem.value);
    } catch (error) {

    }
  } else {
    products.value.push(editedItem.value);
    // alert("재고 정보가 추가되었습니다.");
  }
  close();
}
watch(dialog, val => {
  val || close()
})
watch(dialogDelete, val => {
  val || closeDelete()
})
initialize()
</script>

<template>
  <VDataTable
    :headers="headers"
    :items="products"
    :sort-by="[{ key: 'name_product', order: 'asc' }]"
    class="text-no-wrap"
  >
    <template #top>
      <VToolbar flat>
        <VToolbarTitle>재고관리</VToolbarTitle>

        <VSpacer />

        <VDialog
          v-model="dialog"
          max-width="500px"
        >
          <template #activator="{ props }">
            <VBtn
              color="primary"
              v-bind="props"
            >
              상품 추가
            </VBtn>
          </template>

          <VCard :title="formTitle">
            <VCardText>
              <VRow>
                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="editedItem.name_product"
                    label="상품명"
                  />
                </VCol>
                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="editedItem.quantity"
                    label="재고수량"
                  />
                </VCol>
                <VCol
                  cols="12"
                  sm="6"
                  md="4"
                >
                  <VTextField
                    v-model="editedItem.price"
                    label="가격"
                  />
                </VCol>
              </VRow>
            </VCardText>

            <VCardActions>
              <VSpacer />
              <VBtn
                color="primary"
                @click="close"
              >
                닫기
              </VBtn>
              <VBtn
                color="success"
                @click="save"
              >
                추가
              </VBtn>
            </VCardActions>
          </VCard>
        </VDialog>

        <VDialog
          v-model="dialogDelete"
          max-width="500px"
        >
          <VCard title="Are you sure you want to delete this item?">
            <VCardActions>
              <VSpacer />
              <VBtn
                color="error"
                @click="closeDelete"
              >
                Cancel
              </VBtn>
              <VBtn
                color="success"
                @click="deleteItemConfirm"
              >
                OK
              </VBtn>
              <VSpacer />
            </VCardActions>
          </VCard>
        </VDialog>
      </VToolbar>
    </template>

    <template #item.actions="{ item }">
      <VIcon
        icon="mdi-pencil-outline"
        size="small"
        class="me-2"
        @click="editItem(item.raw)"
      />
      <VIcon
        icon="mdi-delete-outline"
        size="small"
        @click="deleteItem(item.raw)"
      />
    </template>
    <template #no-data>
        <p>등록된 상품이 없습니다.</p>
    </template>
  </VDataTable>
</template>

<script>
export default {
    data() {
        return {
            product: {
                name_product: "",
                quantity: 0,
                price: 0,
            }
        }
    },
    methods: {

    }
}
</script>