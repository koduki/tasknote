<template>
  <div class="container-fluid home" style="text-align: left">
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <div>
        <button
          type="button"
          class="btn btn-outline-secondary"
          style="margin-bottom: -8px;border-bottom:none"
        >
          メイン
        </button>
        <button
          type="button"
          class="btn btn-outline-light"
          style="color: black; margin-bottom: -8px; border-right: solid 1px #AAAAAA"
        >
          TODO
        </button>
        <button
          type="button"
          class="btn btn-outline-light"
          style="color: black; margin-bottom: -8px; border-right: solid 1px #AAAAAA"
        >
          後で読む
        </button>
        <button
          type="button"
          class="btn btn-outline-light"
          style="color: black; margin-bottom: -8px"
        >
          + Add Note
        </button>
      </div>
    </div>

    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <div>
        <img src="/mode-toggle-1.png" />
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
        <markdown-editor
          v-model="tasks.text"
          height="800px"
          outerHeight="auto"
        />
      </main>
    </div>
  </div>
</template>

<script>
import MarkdownEditor from "@/components/MarkdownEditor";
import Tasks from "@/modules/tasks";

export default {
  name: "Home",
  components: {
    MarkdownEditor,
  },
  data() {
    return {
      tasks: new Tasks(),
    };
  },
  created() {
    this.tasks.load(
      () => {
        console.log("load");

        this.tasks.startAutoSave((r2) => {
          let item = document.getElementsByClassName("updated_timestamp")[0];
          item.textContent = "Last saved: " + new Date();
        });
      },
      () => {
        console.log("skip load");
        this.tasks.startAutoSave((r2) => {
          let item = document.getElementsByClassName("updated_timestamp")[0];
          item.textContent = "Last saved: " + new Date();
        });
      }
    );
  },
  beforeDestroy() {
    this.tasks.stopAutoSave();
  },

  methods: {},
};
</script>

<style>
.MarkdownEditor .CodeMirror {
  height: 800px;
}
</style>