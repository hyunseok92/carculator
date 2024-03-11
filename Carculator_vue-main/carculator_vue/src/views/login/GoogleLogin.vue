<template>
    <div></div>
  </template>
  
  <script>
  export default {
    data() {
        return {
            code: "",
        };
    },
    mounted() {
        console.log("code = " + this.$route.query.code);
        this.code = this.$route.query.code;
  
        this.$axios
            .get("/googleLogin/callbacks", {
                params: {
                    code: this.code,
                },
            })
            .then((response) => {           
  
              console.log("callback " + response.status);
  
              if(response.status == 200) {

                sessionStorage.setItem("accessToken", response.headers.access);
                sessionStorage.setItem("refreshToken", response.headers.refresh);
                sessionStorage.setItem("memberCode", response.data.membercode);
                sessionStorage.setItem("userName", response.data.userName);
                sessionStorage.setItem("userEmail", response.data.userEmail);
                sessionStorage.setItem("userRole", response.data.role);
  
                this.$router.push({ name: "main" });
              }
            })
            .catch((err) => {
                alert("로그인 실패");
                console.error(err);
            });
    },
  };
  </script>
  
  <style>
  </style>