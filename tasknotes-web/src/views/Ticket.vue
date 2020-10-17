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
        <h1
          class="h1 editable-box"
          @mouseover="onMouseover4editable($event)"
          @mouseout="onMouseout4editable($event)"
        >
          <span class="editable-box-body" v-show="!this.isEditableName">
            {{ task.name }}
          </span>
          <input
            type="text"
            width="640px"
            v-show="this.isEditableName"
            v-model="task.name"
          />
          <span class="editable-box-btn">
            <b-icon
              icon="pencil-fill"
              scale="0.5"
              v-show="!this.isEditableName"
              @click="toggleEditableName"
            ></b-icon>
            <b-icon
              icon="check"
              v-show="this.isEditableName"
              @click="toggleEditableName"
            ></b-icon>
          </span>
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
                >
                  {{ task.status != "" ? task.status : "Open" }}
                </span>
              </td>
              <th scope="col">期日</th>
              <td>
                <input
                  type="date"
                  v-model="task.dueDate"
                  @change="onDateChange"
                />
              </td>
            </tr>
          </tbody>
        </table>
        <h2 class="h4">Description & Activity:</h2>
        <main
          role="main"
          class="col-md-12 editable-box"
          @mouseover="onMouseover4editable($event)"
          @mouseout="onMouseout4editable($event)"
        >
          <div class="row">
            <div class="editable-box-body col-md-11">
              <div
                v-show="!this.isEditableBody"
                class="wrapper"
                v-html="this.previewText"
              ></div>
              <markdown-editor
                v-show="this.isEditableBody"
                v-model="task.body"
                height="400px"
                outerHeight="400px"
              />
            </div>
            <div class="editable-box-btn col-md-0.8" @click="onEditableBody">
              <b-icon
                icon="pencil-fill"
                scale="1"
                v-show="!this.isEditableBody"
              ></b-icon>
            </div>
          </div>
          <div class="row" style="text-align: right">
            <div class="col-md-11" v-show="this.isEditableBody">
              <b-button style="margin: 2px" @click="onSaveBody">Save</b-button>
              <b-button style="margin: 2px" @click="onCancelBody"
                >Cancel</b-button
              >
            </div>
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
    onEditableBody() {
      this.isEditableBody = true;
    },
    onSaveBody() {
      this.tasks.save(this.task);
      this.isEditableBody = false;
    },
    onCancelBody() {
      this.isEditableBody = false;
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
    onDateChange() {
      this.tasks.save(this.task);
    },
    onMouseover4editable(event) {
      if (!this.isEditableBody) {
        const box = this.findEditableBox(event.target);
        box.body.classList.add("active");
        box.btn.style.display = "unset";
      }
    },
    onMouseout4editable(event) {
      const box = this.findEditableBox(event.target);
      box.body.classList.remove("active");
      box.btn.style.display = "none";
    },
    onClickTicketItem(t) {
      this.task = t;
    },
    findEditableBox(node) {
      while (!node.classList.contains("editable-box")) {
        node = node.parentElement;
      }

      return {
        body: node.getElementsByClassName("editable-box-body")[0],
        btn: node.getElementsByClassName("editable-box-btn")[0],
      };
    },
  },
};
</script>
<style>
.editable-box-body.active {
  border: 1px solid #ccc;
  padding-right: 50px;
  padding-top: 3px;
  padding-bottom: 2px;
}
.editable-box-btn {
  background-color: lightgrey;
  padding: 4px;
  cursor: hand;
  cursor: pointer;
  display: none;
}
</style>