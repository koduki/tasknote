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
            <li class="nav-item" v-for="task in tasks.data" :key="task._id">
              <b-icon icon="clipboard-check"></b-icon>
              {{ task.name }}
            </li>
          </ul>
        </div>
      </nav>

      <main role="main" class="col-md-8">
        <div id="markdownEditor">
          <div class="updated_timestamp"></div>
          <vue-simplemde v-model="tasks.text" ref="markdownEditor" />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import Loading from "vue-loading-overlay";
import "vue-loading-overlay/dist/vue-loading.css";
import VueSimplemde from "vue-simplemde";
import TaskNoteAPI from "@/modules/tasknotes_api";
import Tasks from "@/modules/tasks";

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
      tasks: new Tasks(),
    };
  },
  created() {
    this.tasks.load((r1) => {
      console.log("load");

      this.tasks.startAutoSave((r2) => {
        console.log("save");

        let item = document.getElementsByClassName("updated_timestamp")[0];
        item.textContent = "Last saved: " + new Date();
      });
    });
  },
  beforeDestroy() {
    this.tasks.stopAutoSave();
  },
  mounted() {
    document
      .getElementsByClassName("vue-simplemde")[0]
      .addEventListener("paste", this.onPasteImage);
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
    onCancel: function () {
      console.log("User cancelled the loader.");
    },
  },
};
</script>

<style>
@import "~simplemde/dist/simplemde.min.css";
</style>

<style>
#markdownEditor .CodeMirror {
  height: 800px;
}
</style>