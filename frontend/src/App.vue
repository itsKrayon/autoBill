<template>
  <div id="app">
    <router-view v-if="isRouterAlive" />
  </div>
</template>

<script>
export default {
  name: "App",
  provide() {
    return {
      reload: this.reload,
      dateConversion: this.dateConversion,
    };
  },
  data() {
    return {
      isRouterAlive: true,
    };
  },
  created(){
    //先注释掉方便测试
    //this.$router.replace({path: '/login'})
  },
  methods: {
    // 页面需要刷新时，按照先注入inject: ['reload']，再调用this.reload()的方式实现
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    dateConversion(value){
      let d = new Date(value);
      let date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
      return date;
    },
  },
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
body {
  margin: 0;
  padding: 0;
}
/* 解决el-scrollbar底部出现原生横向滚动条的问题，方法：使用el-scrollbar时添加wrap-class="default-scrollbar__wrap" */
.el-scrollbar__wrap.default-scrollbar__wrap {
  overflow-x: auto;
}
</style>
