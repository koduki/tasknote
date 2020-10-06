<template>
  <div class="container-fluid home">
    <div class="row">
      <nav class="col-md-3 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
          <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
            <h1 class="h2">Task List</h1>
          </div>
          <ul class="nav flex-column" style="text-align: left">
            <li class="nav-item" v-for="task in tasks" :key="task._id">
              <a class="nav-link" href=".">
                <b-icon icon="clipboard-check"></b-icon>
                {{ task.name }}
              </a>
            </li>
          </ul>
        </div>
      </nav>

      <main role="main" class="col-md-8">
        <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
          <h1 class="h2">編集モード</h1>
        </div>

        <div style="text-align: left">
          <vue-simplemde
            v-model="content"
            ref="markdownEditor"
            @input="onInputText"
          />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import VueSimplemde from "vue-simplemde";

export default {
  name: "Home",
  components: {
    VueSimplemde,
  },
  data() {
    return {
      tasks: [],
      content: "# ここにタスク名\n## body\n",
    };
  },
  created: function () {
    // this.fetchTasks();
  },
  methods: {
    fetchTasks() {
      // const uri = process.env.VUE_APP_API_BASE_URL + "/slide";
      const uri = "http://localhost:8080/list";

      const config = {
        headers: {
          // Authorization: "Bearer " + this.$store.state.user.token,
        },
      };
      this.axios.get(uri, config).then((response) => {
        this.tasks = response.data;
      });
    },
    onInputText() {
      console.log(this.content);
      let lines = this.content.toString().split("\n");
      let tasks = [];
      let name = "";
      let body = null;
      console.log(lines);
      lines.forEach((line) => {
        console.log(line);

        if (line.trim().match("^# ")) {
          if (body != null) {
            let task = { name: name, body: body };
            tasks.push(task);
          }
          name = line.trim().replace(/^# /, "");
          body = null;
        } else if (line.trim().match("^## body")) {
          body = "";
        } else if (body != null) {
          body += line + "\n";
        }
      });
      tasks.push({ name: name, body: body });
      this.tasks = tasks;
    },
  },
};
</script>

<style>
@import "~simplemde/dist/simplemde.min.css";
</style>