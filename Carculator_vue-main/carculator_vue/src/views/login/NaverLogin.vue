<template>
  <div></div>
</template>

<script>
export default {
  data() {
      return {
          code: "",
          state: "",
      };
  },
  mounted() {
      console.log("code = " + this.$route.query.code);
      this.code = this.$route.query.code;
      console.log("state = " + this.$route.query.state);
      this.state = this.$route.query.state;

      this.$axios
          .get("/naverLogin/callbacks", {
              params: {
                  code: this.code,
                  state: this.state,
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
              console.error(err);
          });
  },
};
</script>

<style>
</style>