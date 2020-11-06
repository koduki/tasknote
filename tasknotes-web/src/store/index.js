import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

import user from "@/store/modules/user";
import task from "@/store/modules/task";

Vue.use(Vuex);

const store = new Vuex.Store({
  plugins: [
    createPersistedState({
      storage: window.localStorage,
    }),
  ],
  modules: {
    user,
    task,
  },
});

export default store;
