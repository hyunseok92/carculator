import axios from "axios";

const axiosIns = axios.create({
  baseURL: "http://localhost:8888/",
  timeout: 1000000,
});

axiosIns.interceptors.request.use((config) => {
  // 로컬 스토리지에서 토큰을 가져옵니다.
  const token = sessionStorage.getItem("accessToken");
  const refresh = sessionStorage.getItem("refreshToken");

  console.log("클라이언트에서 보내는 토큰 : ", token);
  console.log("클라이언트에서 보내는 리스레시 토큰: ", refresh);

  // 토큰이 존재하는 경우
  if (token) {
    // 요청 헤더를 가져오고, 헤더가 정의되지 않은 경우 빈 객체를 할당합니다.
    config.headers = config.headers || {};

    // 인증 헤더를 설정합니다.
    // ℹ️ JSON.parse는 토큰을 문자열로 변환합니다.
    //config.headers.Authorization = token ? `Bearer ${JSON.parse(token)}` : ''
    config.headers.access = token;
    // 리프레시 토큰을 요청 헤더에 추가합니다.
    config.headers.refresh = refresh;

  }

  // 수정된 구성을 반환합니다.
  return config;
});

axiosIns.interceptors.response.use(
  (response) => {
    console.log("인터 셉터 응답 : " + response.status);
    return response;
  },
  (error) => {
    // 에러 처리
    console.log("인터셉터 에러 : " + error);

    if (error.response.status == 303) {

      console.log("여기로 와야지");
      const token = sessionStorage.getItem("refreshToken");
      const email = sessionStorage.getItem("userEmail");

      // 비동기 함수 내부에서 처리
      try {

        console.log("뭐야이거 안와? = " + email);
        const response = axios.get("http://localhost:8888/newtoken/", email);

        if ("같다오면 상태값" + response.status);

        sessionStorage.setItem("accessToken", response.headers.access);

        window.location.reload();
        return axios(error.config);
      } catch (refreshError) {
        console.error("토큰 갱신 실패:", refreshError);

        sessionStorage.removeItem("accessToken");
        sessionStorage.removeItem("refreshToken");
        sessionStorage.removeItem("memberCode");
        sessionStorage.removeItem("userName");
        sessionStorage.removeItem("userEmail");
        sessionStorage.removeItem("userRole");

        alert("로그인이 만료되었습니다. 다시 로그인 해주세요.");
        window.location.replace("/loginForm");
      }

    }
    else {
      return Promise.reject(error);
    }
  }
);

export default axiosIns;
