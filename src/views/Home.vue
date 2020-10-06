<template>
  <div class="container-fluid home" style="text-align: left">
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 class="h2">編集モード</h1>
    </div>

    <div class="row">
      <nav class="col-md-3 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
          <ul class="nav flex-column">
            <li
              class="nav-item"
              v-for="task in this.$store.state.tasks"
              :key="task._id"
            >
              <a class="nav-link" href=".">
                <b-icon icon="clipboard-check"></b-icon>
                {{ task.name }}
              </a>
            </li>
          </ul>
        </div>
      </nav>

      <main role="main" class="col-md-8">
        <div>
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
      content: "",
    };
  },
  created() {
    this.content = this.$store.state.content;
    if (this.content == "") {
      this.content =
        "# ここにタスク名\n## meta\n- due_date:2020/01/01\n- status:Closed\n## body\nここにコンテンツ。\n# タスク２\n## body\n\n * アイテム１\n# タスク3\n## meta\n- due_date:2020/12/31\n## body\nここにコンテンツ。\n";
    }
  },
  methods: {
    onInputText() {
      console.log(this.content);
      let lines = this.content.toString().split("\n");
      let tasks = [];
      let name = "";
      let isMeta = false;
      let dueDate = "";
      let status = "";
      let body = null;
      console.log(lines);
      lines.forEach((line) => {
        console.log(line);

        if (line.trim().match("^# ")) {
          if (body != null) {
            let task = { name: name, dueDate: dueDate, status: status, body: body };
            tasks.push(task);
          }
          name = line.trim().replace(/^# /, "");
          body = null;
          dueDate = "";
          status = "";
        } else if (line.trim().match("^## meta")) {
          isMeta = true;
        } else if (
          isMeta &&
          (line.trim().match("^- due_date:") ||
            line.trim().match("^\\* due_date:"))
        ) {
          dueDate = line.split("due_date:")[1].trim();
        } else if (
          isMeta &&
          (line.trim().match("^- status:") || line.trim().match("^\\* status:"))
        ) {
          status = line.split("status:")[1].trim();
        } else if (line.trim().match("^## body")) {
          isMeta = false;
          body = "";
        } else if (body != null) {
          body += line + "\n";
        }
      });
      tasks.push({ name: name, dueDate: dueDate, status: status, body: body });
      this.$store.dispatch("updateTasks", tasks);
      this.$store.dispatch("updateContent", this.content);
    },
  },
};
</script>

<style>
@import "~simplemde/dist/simplemde.min.css";
</style>