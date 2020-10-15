<template>
  <div class="container-fluid" style="text-align: left">
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <div>
        <b-button style="margin: 5px">
          <router-link to="/home" style="color: white">Edit Mode</router-link>
        </b-button>
        <b-button variant="success" style="margin: 5px">
          <router-link to="/tickets" style="color: white"
            >Ticket Mode</router-link
          >
        </b-button>
      </div>
    </div>

    <div class="row">
      <nav
        class="col-md-3 d-none d-md-block bg-light sidebar"
        style="margin: 20px"
      >
        <div class="sidebar-sticky">
          <ul class="list-group">
            <li
              @click="onClickTicketItem(t)"
              class="list-group-item"
              v-for="t in this.tasks.data"
              :key="t._id"
            >
              <router-link :to="'/tickets/' + t.id">
                #{{ t.id }} {{ t.name }}
              </router-link>
            </li>
          </ul>
        </div>
      </nav>
      <table class="table col-md-6" style="margin: 20px">
        <h1 class="h1">{{ taskName }}</h1>
        <button @click="toggleEditable">編集/保存</button>

        <h2 class="h4">Details:</h2>
        <table class="table">
          <tbody>
            <tr>
              <th scope="col">ステータス</th>
              <td>{{ taskStatus != "" ? taskStatus : "Open" }}</td>
              <th scope="col">期日</th>
              <td>{{ taskDueDate != "" ? taskDueDate : "-" }}</td>
            </tr>
          </tbody>
        </table>
        <h2 class="h4">Description & Activity:</h2>
        <main role="main" class="col-md-12">
          <div>
            <div
              v-show="!this.isEditable"
              class="wrapper"
              v-html="this.previewText"
            ></div>
            <markdown-editor v-show="this.isEditable" v-model="taskBody" />
          </div>
        </main>
      </table>
    </div>
  </div>
</template>

<script>
import marked from "marked";
import Tasks from "@/modules/tasks";
import MarkdownEditor from "@/components/MarkdownEditor";

export default {
  name: "Ticket",
  components: {
    MarkdownEditor,
  },
  data() {
    return {
      content: "",
      taskName: "",
      taskDueDate: "",
      taskStatus: "",
      taskBody: "",
      tasks: new Tasks(),
      isEditable: false,
    };
  },
  computed: {
    previewText() {
      marked.setOptions({
        renderer: new marked.Renderer(),
        gfm: true,
        tables: true,
        breaks: true,
        pedantic: false,
        smartLists: true,
        smartypants: false,
      });
      return marked(this.taskBody);
    },
  },
  created() {
    this.tasks.data.forEach((t) => {
      if (t.id == this.$route.params.id) {
        this.taskName = t.name;
        this.taskDueDate = t.dueDate;
        this.taskStatus = t.status;
        this.taskBody = t.body;
      }
    });
  },

  methods: {
    toggleEditable() {
      if (this.isEditable) {
        this.tasks.data.forEach((t) => {
          if (t.id == this.$route.params.id) {
            t.body = this.taskBody;
            this.tasks.save()
          }
        });
      }
      this.isEditable = !this.isEditable;
    },
    onClickTicketItem(t) {
      this.taskName = t.name;
      this.taskDueDate = t.dueDate;
      this.taskStatus = t.status;
      this.taskBody = t.body;
    },
  },
};
</script>