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
        <h1 class="h1">
          <span v-show="!this.isEditableName">{{ task.name }}</span>
          <input
            type="text"
            width="640px"
            v-show="this.isEditableName"
            v-model="task.name"
          />
          <b-button class="mb-2">
            <b-icon
              icon="pencil"
              v-show="!this.isEditableName"
              @click="toggleEditableName"
            ></b-icon>
            <b-icon
              icon="check"
              v-show="this.isEditableName"
              @click="toggleEditableName"
            ></b-icon>
          </b-button>
        </h1>
        <h2 class="h4">Details:</h2>
        <table class="table">
          <tbody>
            <tr>
              <th scope="col">ステータス</th>
              <td>
                <span
                  @click="toggleStatus"
                  style="cursor: hand; cursor: pointer"
                  >{{ task.status != "" ? task.status : "Open" }}</span
                >
              </td>
              <th scope="col">期日</th>
              <td>{{ task.dueDate != "" ? task.dueDate : "-" }}</td>
            </tr>
          </tbody>
        </table>
        <h2 class="h4">
          Description & Activity:
          <b-button size="sm" class="mb-2">
            <b-icon
              icon="pencil"
              v-show="!this.isEditableBody"
              @click="toggleEditableBody"
            ></b-icon>
            <b-icon
              icon="check"
              v-show="this.isEditableBody"
              @click="toggleEditableBody"
            ></b-icon>
          </b-button>
        </h2>
        <main role="main" class="col-md-12">
          <div>
            <div
              v-show="!this.isEditableBody"
              class="wrapper"
              v-html="this.previewText"
            ></div>
            <markdown-editor v-show="this.isEditableBody" v-model="task.body" />
          </div>
        </main>
      </table>
    </div>
    <div class="row">
      <div class="updated_timestamp"></div>
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
      tasks: new Tasks(),
      task: null,
      isEditableName: false,
      isEditableBody: false,
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
      return marked(this.task.body);
    },
  },
  created() {
    this.tasks.data.forEach((t) => {
      if (t.id == this.$route.params.id) {
        this.task = t;
      }
    });
  },
  beforeDestroy() {
    this.tasks.stopAutoSave();
  },
  methods: {
    toggleEditableBody() {
      if (this.isEditableBody) {
        this.tasks.save(this.task);
      }
      this.isEditableBody = !this.isEditableBody;
    },
    toggleEditableName() {
      if (this.isEditableName) {
        this.tasks.save(this.task);
      }
      this.isEditableName = !this.isEditableName;
    },
    toggleStatus() {
      console.log(this.task.status);

      switch (this.task.status) {
        case "Open":
          this.task.status = "In Progress";
          break;
        case "In Progress":
          this.task.status = "Closed";
          break;
        case "Closed":
          this.task.status = "Open";
          break;
        default:
          this.task.status = "In Progress";
      }
      this.tasks.save(this.task);
    },
    onClickTicketItem(t) {
      this.task = t;
    },
  },
};
</script>