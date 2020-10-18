export default {
  namespaced: true,
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
};
