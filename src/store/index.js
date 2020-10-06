import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    tasks: [],
  },
  mutations: {
    updateTasks(state, tasks) {
      state.tasks = tasks;
    }
  },
  actions: {
    updateTasks(context, tasks) {
      context.commit("updateTasks", tasks);
    }
  },
  modules: {},
});
