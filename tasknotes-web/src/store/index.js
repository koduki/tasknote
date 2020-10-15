import Vue from "vue";
import Vuex from "vuex";

import user from "@/store/modules/user";
import task from "@/store/modules/task";

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    user,
    task,
  },
});

export default store;
