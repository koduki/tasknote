<template>
  <div class="container-fluid home" style="text-align: left">
    <loading
      :active.sync="isLoading"
      :can-cancel="true"
      :on-cancel="onCancel"
      :is-full-page="fullPage"
    ></loading>
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <div>
        <b-button variant="success" style="margin: 5px">
          <router-link to="/home" style="color: white">Edit Mode</router-link>
        </b-button>
        <b-button style="margin: 5px">
          <router-link to="/tickets" style="color: white"
            >Ticket Mode</router-link
          >
        </b-button>
      </div>
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
          <div class="updated_timestamp">AUTO</div>
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
import Loading from "vue-loading-overlay";
import "vue-loading-overlay/dist/vue-loading.css";
import VueSimplemde from "vue-simplemde";
import Auth from "@/modules/auth";
import Task from "@/modules/task";
import TaskNoteAPI from "@/modules/task_note_api";
import Template from "@/modules/template";

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
      prevContent: "",
    };
  },
  created() {
    this.content = Task.toContent(this.$store.state.tasks);
    if (this.content == "") {
      this.content = Template.defaultEditorContents();
    }

    TaskNoteAPI.callLoad((response) => {
      this.content = response.data.text;
      const tasks = Task.parse(response.data.text);
      this.$store.dispatch("updateTasks", tasks);
    });
    this.timer = setInterval(this.autosave, 5 * 1000);
  },
  mounted() {
    // regist paste event
    document
      .getElementsByClassName("vue-simplemde")[0]
      .addEventListener("paste", this.onPasteImage);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    insertText(term) {
      const editor = this.$refs.markdownEditor.simplemde;

      const cm = editor.codemirror;
      var startPoint = cm.getCursor("start");
      var endPoint = cm.getCursor("end");

      cm.replaceSelection(term);
      cm.setSelection(startPoint, endPoint);
      cm.focus();
    },

    onPasteImage(event) {
      console.log(event);

      const items = event.clipboardData.items;
      for (var i = 0; i < items.length; i++) {
        const item = items[i];
        const file = item.getAsFile();
        if (item.type.indexOf("image") != -1) {
          event.preventDefault();
          this.isLoading = true;
          TaskNoteAPI.callUploadImage(
            file,
            (response) => {
              this.insertText("![image.png](" + response.data.url + ")");
              this.isLoading = false;
            },
            (error) => {
              this.isLoading = false;
            }
          );
        }
      }
    },

    autosave() {
      if (this.prevContent != this.content) {
        this.prevContent = this.content;
        TaskNoteAPI.callSave((response) => {
          console.log(response.data);
          document.getElementsByClassName("updated_timestamp")[0].textContent="Last saved: " + new Date();
        }, this.content);
      } else {
        console.log("skip autosave");
      }
    },

    onInputText() {
      let tasks = Task.parse(this.content);
      this.$store.dispatch("updateTasks", tasks);
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