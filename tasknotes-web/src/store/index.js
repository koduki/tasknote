import Vue from "vue";
import Vuex from "vuex";

import user from "@/store/modules/user";

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    user,
  },
  state: {
    tasks: [],
  },
  mutations: {
    updateTasks(state, tasks) {
      state.tasks = tasks;
    },
  },
  actions: {
    updateTasks(context, tasks) {
      context.commit("updateTasks", tasks);
    },
  },
});

export default store;
