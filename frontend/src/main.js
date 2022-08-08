// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App'
import router from './router'

Vue.use(ElementUI);

// axios 配置
let axios = require('axios');
// axios挂载到prototype，全局可以使用this.$axios访问
Vue.prototype.$axios = axios;
axios.defaults.baseURL = '/api';
axios.defaults.withCredentials = true;
axios.defaults.headers.post['Content-Type'] = "application/json;charset=UTF-8";
axios.interceptors.request.use(
  config => {
    if (localStorage.getItem("token") != null) {
      config.headers["token"] = localStorage.getItem("token");
      config.headers.common['Authorization'] = "Bearer " + localStorage.getItem("token");
    }

    return config;
  },
  err => Promise.reject(err)
);
/*
// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {

    console.log("token err:"+error);
    alert(error.response.data.message);
    if(error.response.status===403) {

      // 清除token 如果不是login, 跳转至login
      localStorage.setItem('token','');
      router.replace({
        path: '/login',
        query: { redirect: router.currentRoute.path }
      })
    }

    return Promise.reject(error)
  }


);


Vue.config.productionTip = false;
/*
/* eslint-disable no-new
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});
*/
new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
