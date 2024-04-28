import App from './App'
import uView from 'uview-ui';
import store from './store/index.js'
Vue.use(uView)
Vue.prototype.$elyasApi = 'http://localhost:7007/'
Vue.prototype.$store = store;

// 权限验证
Vue.prototype.checkAuth = (callback) =>{
	if (!store.state.loginStatus) {
		uni.showToast({
			title:'请先登录'
		});
		
		return uni.navigateTo({
			url:'../login/login'
		})
		
	}
	callback()
}



// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
	store,
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif