<template>
  <VCard>
  <div class="notice-container">
    <div class="notice-header">
      <h2>공지사항</h2>
      <VBtn v-if="loginok == 'ROLE_ADMIN'"
        color="primary" @click="noticeWrite()">
        공지글 작성
      </VBtn>
    </div>
    <ul>
      <li v-for="(item, index) in noticeList" :key="index" class="notice-item">
        <div @click="noticeDetail(item.notice_no)">
          <strong>번호:</strong> {{ item.notice_no }}<br>
          <strong>제목:</strong> {{ item.notice_title }}<br>
          <strong>조회수:</strong> {{ item.notice_count }}<br>
          <!-- <strong>날짜:</strong> {{ item.notice_date }}<br> -->
        </div>
      </li>
    </ul>
  </div>
</VCard>
</template>

<script>
import router from '@/router';
import axios from 'axios';
import noticeWriteVue from './noticeWrite.vue';

export default {
 
  props: {
      notice_no: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      requestBody: {},
      notice_no: '',
      membercode: '',
      notice_title: '',
      notice_date: '',
      notice_content: '',
      notice_count: '',
      notice_target: '',
      noticeList: [],
      loginok: sessionStorage.getItem("userRole"),
    };
  },
  mounted() {
    this.callNotice();
    
    
  },
  methods: {
    async callNotice() {
      try {
        const response = await axios.get('http://localhost:8888/notice');
        this.noticeList = response.data;
      } catch (error) {
        console.error('데이터 가져오기 오류:', error);
      }
    },
      noticeWrite() {
        this.$router.push({name: 'noticeWrite1'});
      },
      noticeDetail(notice_no) {
        console.log("공지번호 :" + notice_no)
        this.requestBody.notice_no = notice_no
        this.$router.push({ name: 'noticeDetail',
         params:{ notice_no: this.requestBody.notice_no }});

      },
  },
};
</script>

<style scoped>
.notice-container {
  width: 100%;
  max-width: 800px;
  margin: 20px auto;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.notice-header h2 {
  font-size: 24px;
  color: #333;
}

.write-button {
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  background-color: #007bff;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.write-button:hover {
  background-color: #0056b3;
}

.notice-item {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 20px;
  padding: 15px;
  transition: background-color 0.3s ease;
}

.notice-item:hover {
  background-color: #e0e0e0;
}

.notice-item div {
  font-size: 16px;
}

.notice-item strong {
  font-weight: bold;
  margin-right: 5px;
  color: #333;
}
</style>
