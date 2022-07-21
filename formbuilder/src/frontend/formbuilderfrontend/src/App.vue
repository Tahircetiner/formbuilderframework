<template>
  <div id="app">
    <h1>Generate formular</h1>

    <ComponentGenerator/>
  </div>
</template>

<script>
import ComponentGenerator from './components/ComponentGenerator.vue'

export default {
  name: 'App',
  components: {
    ComponentGenerator
  }
}
</script>

<style>

  @import './assets/generalStyles/generalStyles.scss';

</style>
