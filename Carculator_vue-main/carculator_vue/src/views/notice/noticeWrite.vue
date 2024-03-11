<script setup>
import { QuillEditor } from '@vueup/vue-quill';

// breadcrumbs

const breadcrumbs = [
  {
    title: 'Home',
    disabled: false,
    to: { path: '/' },
  },
  {
    title: '공지사항',
    disabled: true,
  },  
  {
    title: '글쓰기 / 글수정',
    disabled: true,
  }, 
]
</script>

<template>
  <VCard>
    <!-- Breadcrumbs -->
    <VCol cols="12">
      <VBreadcrumbs :items="breadcrumbs" divider="-" class="px-0" />
    </VCol>

    <VTextarea
      clearable
      v-model="notice_title"
      clear-icon="mdi-close-circle-outline"
      label="제목을 입력해주세요"
      cols="1"
      rows="1"
    />

    <!-- Add some margin-top to create space between notice_title and notice_content -->
    <div class="board-contents mt-4">
      <VTextarea
        clearable
        v-model="notice_content"
        clear-icon="mdi-close-circle-outline"
        label="내용을 입력해주세요"
        cols="10"
        rows="10"
      />
    </div>

    <!-- Add some margin-top to create space between notice_content and buttons -->
    <div style="width: 100%; display: flex; justify-content: flex-end; margin-top: 20px;">
      <VBtn color="info" v-on:click="fnSave" div class="text-right">
        작성 완료
      </VBtn>
      <VBtn color="error" :to="{ name: 'notice' }" div class="text-right ml-4">
        작성 취소
      </VBtn>
    </div>
  </VCard>
</template>


<style lang="scss">
@import "@vueup/vue-quill/dist/vue-quill.snow.css";
@import "@/styles/libs/quill-editor";

.title-input {
  font-size: 18px;
  margin-bottom: 10px;
  width: 100%;
  border: 1px solid #000; /* Add a black border */
  padding: 10px;
}

.content-input {
  font-size: 16px;
  width: 100%;
  height: 200%;
  border: 1px solid #000; /* Add a black border */
  padding: 10px;
}

.my-custom-spacing-class {
  margin-bottom: 25px; /* Adjust the spacing as needed */
}

.text-right .v-btn {
  margin-right: auto;
}
</style>

<script>
import axios from "@axios";
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
    }
  },
  mounted() {
    this.detailInfo(); // Fix: Call this.detailInfo() instead of detailInfo()    
  },
  methods: {
    detailInfo() { // Fix: Remove the notice_no parameter
      

      if (this.notice_no != undefined) {
      axios.get(`/notice/${this.notice_no}`)
        .then((res) => {
          console.log(res.data)
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
      }// if
      
    },
    fnSave() {

      
      
      let apiUrl = "//localhost:8888/notice";
      let form = {
        "notice_title": this.notice_title, // reference the data property using this
        "notice_content": this.notice_content,
        "notice_no":this.notice_no,
        "notice_count":(this.notice_count + 1),
        "membercode": sessionStorage.getItem("memberCode"),
        "notice_target": "ALL"
      };

        if (this.notice_no == undefined) {
        // INSERT
        axios.post("http://localhost:8888/notice", form)
          .then((res) => {
            alert('글이 저장되었습니다.');
            this.$router.push({ name: 'notice' });
          }).catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.');
            }
          });

    }else{
      //UPDATE
      this.$axios.patch(apiUrl, form)
          .then((res) => {
            alert('글이 수정되었습니다.')
            this.$router.push({name: 'notice' });
          }).catch((err) => {
            if (err.message.indexOf('Network Error') > -1) {
              alert('네트워크가 원활하지 않습니다.\n잠시 후 다시 시도해주세요.')
            }
          });
    } //else
  },
},

};
</script>