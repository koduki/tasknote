export default {
  namespaced: true,
  state: {
    currentNote: "tasks",
    notes: { },
  },
  mutations: {
    updateTasks(state, tasks) {
      state.notes[state.currentNote] = tasks;
    },
    updateCurrent(state, current) {
      state.currentNote = current;
    },
  },
  actions: {
    updateTasks(context, tasks) {
      context.commit("updateTasks", tasks);
    },
    updateCurrent(context, current) {
      context.commit("updateCurrent", current);
    },
  },
};
