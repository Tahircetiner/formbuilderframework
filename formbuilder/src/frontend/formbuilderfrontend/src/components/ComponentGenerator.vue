<template>
  <div class="container">
    <div class="container-fluid"></div>
    <select v-model="fieldName" v-on:change="generateField()">
      <option  value="<input type='text' class='form-control col-lg-12'/>">Eingabefeld (Text)</option>
      <option value="<input type='date' class='form-control col-lg-12'/>">Date</option>
      <option value="<input type='number' class='form-control col-lg-12'/>">Eingabefeld (Nummer)</option>
      <option value="">Dropdown</option>
    </select>
    <form class="customForm">
      <div v-for="(field, index) in htmlFields" v-bind:key="field + index" v-html="field" class="col-lg-5"></div>
    </form>
    <button type="button" class="btn btn-outline-primary" style="margin-top:30px;" v-on:click="sendFilledData();">Primary</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      htmlFields:[],
      fieldName: "",
      fieldStrings: []

    }
  },
  name: 'ComponentGenerator',
  props: {
  },

  methods: {
    generateField: function (){
      this.htmlFields.push(this.fieldName);
    },
    sendFilledData: function (){
      //this.cleanData();
      axios
          .post("http://localhost:8081/api/storeInputFields", this.htmlFields)
          .then(function (){
            window.location.href = "http://localhost:8081/api/createFormularFileZip";
          });
    },
    cleanData: function (){
      this.htmlFields.forEach((htmlField) => {
        this.fieldStrings.push(htmlField.replaceAll("<", "").replaceAll(">",""));
        console.log(htmlField);
      });
    }
  }
}
</script>

<style scoped>
</style>
