// Components
import App from '@/App.vue'
import '@/plugins/apis/db'
import axios from "axios"
import { createApp } from 'vue'

// styles
import '@/styles/index.scss'

// user custom style file
import '@/styles/user-custom/style.scss'

// Plugins
import { registerPlugins } from '@/plugins/vuetify/index'

if (window.Kakao && !window.Kakao.isInitialized()) {
    // 여기서 YOUR_APP_KEY는 Kakao Developers에서 발급받은 앱 키입니다.
    window.Kakao.init('f4ae97bdb9a0fdd7d26770b9a5369071');
  }

const app = createApp(App)
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$serverUrl = 'http://localhost:8888'
registerPlugins(app)
app.mount('#app')
