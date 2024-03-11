<template>
  <VCard>
  <div class="view" style="display: flex;">
    <div class="repList">
      <h2>상세 정보 페이지</h2>
      &nbsp;&nbsp;
      <p>번호: {{ notice_no }}</p>
      <p>제목: {{ notice_title }}</p>
      <p>조회수: {{ notice_count }}</p>
      <!-- <p>날짜: {{ notice_date }}</p> -->
      <p>내용: {{ notice_content }}</p>
    </div>
  </div>
  <div class="d-flex flex-wrap gap-4 button-container">
    <button class="w3-button w3-round w3-blue-gray" @click="fnUpdate" v-if="loginok == 'ROLE_ADMIN'">수정</button>
    <button class="w3-button w3-round w3-red" @click="fnDelete" v-if="loginok == 'ROLE_ADMIN'">삭제</button>
    <button class="w3-button w3-round w3-gray" @click="fnList">목록</button>
  </div>
</VCard>
</template>

<script>
export default {
  data() {
    return {
      requestBody: {},
      notice_no: this.$route.params.notice_no,
      membercode: '',
      notice_title: '',
      notice_date: '',
      notice_content: '',
      notice_count: '',
      notice_target: '',
      loginok: sessionStorage.getItem("userRole"),
    };
  },
  mounted() {
    this.detailInfo();
  },
  methods: {
    detailInfo() {
      this.$axios
        .get(`/notice/${this.notice_no}`)
        .then((res) => {
          console.log(res.data);
          this.membercode = res.data.membercode;
          this.notice_title = res.data.notice_title;
          this.notice_date = res.data.notice_date;
          this.notice_content = res.data.notice_content;
          this.notice_count = res.data.notice_count;
          this.notice_target = res.data.notice_target;
        })
        .catch((err) => {
          console.error(err);
        });
    },
    fnList() {
      this.$router.push({ name: 'notice' });
    },
    fnDelete() {
      if (!confirm('삭제하시겠습니까?')) return;

      this.$axios
        .delete(`http://localhost:8888/notice/${this.notice_no}`, {})
        .then(() => {
          alert('삭제되었습니다.');
          this.$router.push({ name: 'notice' });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    fnUpdate() {
      this.requestBody.notice_no = this.notice_no;
      this.$router.push({
        name: 'noticeWrite2',
        params: { notice_no: this.requestBody.notice_no },
      });
    },
  },
};
</script>

<style scoped>
.view {
    margin: 20px;
  }

  .repList {
    padding: 20px;
    
    border-radius: 10px;
  }

  .button-container {
    margin: 20px;
    border-radius: 10px;
    padding: 20px;
    margin-top: 20px;
  }
.w3-button {
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.w3-blue-gray {
  background-color: #607d8b;
  color: #fff;
}

.w3-red {
  background-color: #f44336;
  color: #fff;
}

.w3-gray {
  background-color: #757575;
  color: #fff;
}

.w3-button:hover {
  cursor: pointer;
  background-color: #546e7a;
}
</style>
