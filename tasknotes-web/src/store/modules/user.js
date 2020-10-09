export default {
  namespaced: true,
  state: {
    id: "",
    name: "",
    token: "",
    pic: "",
    timestamp: 0,
  },
  mutations: {
    store(state, user) {
      state.id = user.id;
      state.token = user.token;
      state.name = user.name;
      state.pic = user.pic;
      state.timestamp = new Date().getTime();
    },
    drop(state) {
      state.id = state.token = state.name = state.pic = "";
    },
    reflesh(state, token) {
      state.token = token;
      state.timestamp = new Date().getTime();
    },
  },
  actions: {
    store(context, user) {
      context.commit("store", user);
    },
    drop(context) {
      context.commit("drop");
    },
    reflesh(context, token) {
      context.commit("reflesh", token);
    },
  },
};
