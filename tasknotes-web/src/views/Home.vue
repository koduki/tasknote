<template>
  <div class="container-fluid home" style="text-align: left">
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <div>
        <span v-for="(noteName, key) in notes" :key="noteName.key">
          <button
            type="button"
            :class="[
              tasks.current === key
                ? 'btn note-tab active'
                : 'btn note-tab inactive',
            ]"
            @click="changeTab"
          >
            {{ noteName }}
          </button>
        </span>
        <span>
          <button type="button" class="btn note-add">+ Add Note</button>
        </span>
      </div>
    </div>

    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <router-link to="/tickets" style="color: white"
        ><img src="/mode-toggle-1.png"
      /></router-link>
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
      notes: [],
    };
  },
  created() {
    const self = this;
    this.tasks.list(
      (res) => {
        self.notes = res;
        console.log(self.notes.todo);
      },
      (res) => {}
    );
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

  methods: {
    changeTab: function (event) {
      const item = event.target.textContent.trim();
      for (let key in this.notes) {
        if (this.notes[key] === item) {
          this.tasks.current = key;
          break;
        }
      }
    },
  },
};
</script>

<style>
.note-tab {
  cursor: pointer;
  color: black;
}
.note-tab.active {
  background-color: #e4e4e4;
  color: #6c757d;
  border-color: #6c757d;
  border: 1px solid;
  margin-bottom: -8px;
  border-bottom: none;
}
.note-tab.active:hover {
  color: #ffffff;

  background-color: #6c757d;
}
.note-tab.inactive {
  margin-bottom: -8px;
  border-right: solid 1px #aaaaaa;
}
.note-tab.inactive:hover {
  background-color: #e4e4e4;
}
.note-add {
  color: black;
  margin-bottom: -8px;
}
.note-add:hover {
  background-color: #e4e4e4;
}

.MarkdownEditor .CodeMirror {
  height: 800px;
}
</style>