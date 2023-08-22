<template>
  <NavBar />
  <router-view/>
</template>

<script>
import NavBar from './components/NavBar.vue'
// App组件中需要使用到bootstrap样式，导入进来
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
export default {
  name : 'App',
  components : {
    NavBar
  },
  setup: () => {
    

    return { 
    }
  }
}

</script>


<style>
body {
  background-image: url("./assets/images/background.png");
  background-size: cover;
}

</style>
