import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    tasks: [],
    content: ""
  },
  mutations: {
    updateTasks(state, tasks) {
      state.tasks = tasks;
    },
    updateContent(state, content) {
      state.content = content;
    }
  },
  actions: {
    updateTasks(context, tasks) {
      context.commit("updateTasks", tasks);
    },
    updateContent(context, content) {
      context.commit("updateContent", content);
    },
  },
  modules: {},
});
