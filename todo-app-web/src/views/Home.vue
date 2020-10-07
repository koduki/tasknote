<template>
  <div class="container-fluid home" style="text-align: left">
    <loading
      :active.sync="isLoading"
      :can-cancel="true"
      :on-cancel="onCancel"
      :is-full-page="fullPage"
    ></loading>
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 class="h2">edit-mode</h1>
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
            @initialized="onInitializedEditor"
          />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import VueSimplemde from "vue-simplemde";
import Loading from "vue-loading-overlay";
import "vue-loading-overlay/dist/vue-loading.css";

export default {
  name: "Home",
  components: {
    VueSimplemde,
    loading: Loading,
  },
  data() {
    return {
      isLoading: false,
      fullPage: true,
      timer: "",
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
### ここにコンテンツ。
![image.png](https://storage.googleapis.com/ajfgeay8733/image/bb0c1b12-b2bc-4443-bbe3-9dd12870a2c1.png)

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

    setTimeout(this.autosave, 1);
    this.timer = setInterval(this.autosave, 60 * 1000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    onInitializedEditor() {
      console.log("onInitializedEditor");

      // regist paste event
      const editor = this.$refs.markdownEditor.simplemde;
      document
        .getElementsByClassName("vue-simplemde")[0]
        .addEventListener("paste", (event) => {
          console.log(event);

          const items = event.clipboardData.items;
          for (var i = 0; i < items.length; i++) {
            const item = items[i];
            const file = item.getAsFile();
            if (item.type.indexOf("image") != -1) {
              event.preventDefault();

              const uri = "https://tasknote-6fbhe65axa-uc.a.run.app/tasks/image";

              const data = new FormData();
              data.append("file", file);
              const config = { headers: {} };
              this.isLoading = true;
              this.axios
                .post(uri, data, config)
                .then((response) => {
                  const cm = editor.codemirror;
                  var startPoint = cm.getCursor("start");
                  var endPoint = cm.getCursor("end");

                  const term = "![image.png](" + response.data.url + ")";
                  cm.replaceSelection(term);
                  cm.setSelection(startPoint, endPoint);
                  cm.focus();
                  this.isLoading = false;
                  // this.$router.push({ name: "Home" });
                })
                .catch((error) => {
                  this.isLoading = false;
                  // this.message = `status: ${error.response.status}, message: ${error.response.data}`;
                });
            }
          }
        });

      console.log(this.$refs.markdownEditor.simplemde);
    },
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
        // console.log(line);

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
    autosave() {
      const uri = "https://tasknote-6fbhe65axa-uc.a.run.app" + "/tasks/save";

      const config = {
        headers: {
          // Authorization: "Bearer " + this.$store.state.user.token,
        },
      };
      this.axios.post(uri, config).then((response) => {
        console.log(response.data);
      });
    },
    onInputText() {
      let tasks = this.parseToTask(this.content);
      this.$store.dispatch("updateTasks", tasks);
    },
    onPaste(event) {
      console.log("on paste", event);
    },
    onCancel: function () {
      console.log("User cancelled the loader.");
    },
  },
};
</script>

<style>
@import "~simplemde/dist/simplemde.min.css";
</style>