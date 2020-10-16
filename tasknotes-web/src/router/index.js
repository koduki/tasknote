import Vue from "vue";
import VueRouter from "vue-router";
import Login from "@/views/Login.vue";
import Home from "@/views/Home.vue";
import Tickets from "@/views/Tickets.vue";
import Ticket from "@/views/Ticket.vue";

// store
import Store from "@/store";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Login",
    component: Login,
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
    meta: { requiresAuth: true },
  },
  {
    path: "/tickets",
    name: "Tickets",
    component: Tickets,
    meta: { requiresAuth: true },
  },
  {
    path: "/tickets/:id",
    name: "Ticket",
    component: Ticket,
    meta: { requiresAuth: true },
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  if (
    to.matched.some((record) => record.meta.requiresAuth) &&
    !Store.state.user.token
  ) {
    next({ path: "/", query: { redirect: to.fullPath } });
  } else {
    next();
  }
});

export default router;
