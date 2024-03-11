<script setup>
import axios from '@axios';
import { onMounted, ref } from "vue";
import { VDataTable } from 'vuetify/labs/VDataTable';
import { useRouter } from 'vue-router';

const router = useRouter();

const search = ref('')

// headers
const headers = [
  {
    title: '이름',
    key: 'name',
  },
  {
    title: '이메일',
    key: 'email',
  },
  {
    title: '전화번호',
    key: 'phone',
  },
  {
    title: '종류',
    key: 'grade',
  },
  {
    title: '상태',
    key: 'status',
  },
  {
    title: '승인/활성/비활성',
    key: 'membercode',
  }
]

// users
const users = ref([])

const fetchUsers = async () => {
  try {
    const response = await axios.get("http://localhost:8888/member/adminSelect");
    const data = response.data;

    users.value = data;

    data.forEach((users) => {
      const status = users.status;
      const role = users.grade;

      if (status == '0')
        users.status = "활성"
      else if (status == "1")
        users.status = "비활성"
      else if (status == "2")
        users.status = "승인대기"

      if (role == "ROLE_SHOP") {
        users.grade = "정비소"
      } else {
        users.grade = "일반"
      }

    });

  } catch (error) {
    console.log("adminSelectMember" + error);
  }
}


onMounted(async () => {
  fetchUsers()
})

// breadcrumbs
const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { name: 'main' },
  },
  {
    title: '관리자',
    disabled: true,
  },
  {
    title: '회원조회',
    disabled: true,
  },
]

const resolveChipColor = value => {
  if (value == '활성')
    return 'success'
  else if (value == '비활성')
    return 'error'
  else
    return 'warning'
}

const fetchStatus = async (value) => {

  const membercode = value;

  // Axios를 사용한 POST 요청
  const response = await axios.post("http://localhost:8888/member/status", membercode);
  try {
    if (response.status == 200) {
      fetchUsers();
    }
  } catch (error) {
    alert("회원 상태 수정 실패");
    console.log("status change error");
  }
}

const moveToDetail = async (value) => {

  console.log("movdedetail" + value);
  try {
    
    router.push({ path: '/member/memberInfo', query: { membercode : value } });

  } catch(error) {
    console.log("moveDetail 실패" + error);
  }

}
</script>

<template>
  <VRow>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs :items="breadcrumbs" divider="-" class="px-0" />
    </VCol>

    <VCol cols="12">
      <VCard>
        <VCardItem>
          <template #append>
            <div style="width: 25rem;">
              <VTextField v-model="search" prepend-inner-icon="mdi-magnify" placeholder="Search..." />
            </div>
          </template>
        </VCardItem>

        <!-- Datatable -->
        <VDataTable :search="search" :headers="headers" :items="users" show-select
          class="table-borderless text-medium-emphasis rounded-0 text-no-wrap">

          <!-- name -->
          <template #item.name="{ item }">
            <div>
              <VAvatar size="32" color="primary" :image="item.raw.avatar" class="me-2">
                <VImg v-if="item.raw.avatar" :src="item.raw.avatar" />
                <span v-else>{{ item.raw.name.charAt(0).toUpperCase() }}</span>
              </VAvatar>

              <span class="text-high-emphasis font-weight-medium">{{ item.raw.name }}</span>
            </div>
          </template>

          <!-- status -->
          <template #item.status="{ item }">
            <VChip label density="comfortable" :color="resolveChipColor(item.raw.status)">
              <span class="text-capitalize text-xs">{{ item.raw.status }}</span>
            </VChip>
          </template>

          <!-- actions -->
          <template #item.membercode="{ item }">
            <div>
              <VBtn icon variant="plain" size="x-small" @click="moveToDetail(item.raw.membercode)">
                <VIcon size="20" icon="mdi-eye-outline" />
                <VTooltip activator="parent">
                  정보 보기
                </VTooltip>
              </VBtn>

              <VBtn icon variant="plain" size="x-small" @click="fetchStatus(item.raw.membercode)">
                <VIcon size="20" icon="mdi-delete-outline" />
                <VTooltip activator="parent">
                  승인/활성/비활성
                </VTooltip>
              </VBtn>
            </div>
          </template>
        </VDataTable>
      </VCard>
    </VCol>
  </VRow>
</template>
