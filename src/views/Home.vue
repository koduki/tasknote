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
    this.content = this.parseToContent(this.$store.state.tasks);
    if (this.content == "") {
      this.content = `# ここにタスク名
## meta
- due_date:2020/12/31
- status:Closed

## body
ここにコンテンツ。

# タスク２

## body
* アイテム１

# タスク3
## meta
- due_date:2020/12/31

## body
ここにコンテンツ。
`;

    }
  },
  methods: {
    parseToContent(tasks) {
      return tasks
        .map((t) => {
          let name = "# " + t.name;
          let meta =
            t.dueDate != "" || t.status != ""
              ? "\n## meta\n" +
                "- due_date:" +
                t.dueDate +
                "\n" +
                "- status:" +
                t.status
              : "";
          let body = "\n## body\n" + t.body;
          return name + "\n" + meta + "\n" + body.replace(/\n$/, "");
        })
        .join("\n")
        .replace(/\n\n+/g, "\n\n");
    },
    parseToTask(content) {
      let lines = content.toString().split("\n");
      let tasks = [];
      let name = "";
      let isMeta = false;
      let dueDate = "";
      let status = "";
      let body = null;
      let id = 0;
      lines.forEach((line) => {
        console.log(line);

        if (line.trim().match("^# ")) {
          if (body != null) {
            id++;
            let task = {
              id: id,
              name: name,
              dueDate: dueDate,
              status: status,
              body: body,
            };
            tasks.push(task);
          }
          name = line.trim().replace(/^# /, "");
          body = null;
          dueDate = "";
          status = "";
        } else if (line.trim().match("^## meta$")) {
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
        } else if (line.trim().match("^## body$")) {
          isMeta = false;
          body = "";
        } else if (body != null) {
          body += line + "\n";
        }
      });
      id++;
      tasks.push({
        id: id,
        name: name,
        dueDate: dueDate,
        status: status,
        body: body,
      });

      return tasks;
    },
    onInputText() {
      let tasks = this.parseToTask(this.content);
      this.$store.dispatch("updateTasks", tasks);
    },
  },
};
</script>

<style>
@import "~simplemde/dist/simplemde.min.css";
</style>