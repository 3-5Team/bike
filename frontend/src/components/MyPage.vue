<template>

<v-data-table
    :headers="headers"
    :items="myPage"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'MyPage',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "bikeId", value: "bikeId" },
            { text: "bikeNm", value: "bikeNm" },
            { text: "reserveId", value: "reserveId" },
            { text: "userId", value: "userId" },
            { text: "userNm", value: "userNm" },
            { text: "reserveStore", value: "reserveStore" },
            { text: "reserveDt", value: "reserveDt" },
            { text: "payId", value: "payId" },
            { text: "price", value: "price" },
            { text: "payStatus", value: "payStatus" },
            { text: "deliveryId", value: "deliveryId" },
            { text: "deliveryStatus", value: "deliveryStatus" },
            { text: "deliveryDt", value: "deliveryDt" },
            { text: "reserveStatus", value: "reserveStatus" },
        ],
        myPage : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/mypages')

      this.myPage = temp.data._embedded.mypages;

    },
    methods: {
    }
  }
</script>

