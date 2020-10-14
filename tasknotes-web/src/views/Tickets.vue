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
      <div class="updated_timestamp"></div>
    </div>

    <div class="row">
      <table id="task-tickets" class="table col-md-6" style="margin: 20px">
        <thead>
          <tr>
            <th scope="col" @click="sortByCol('id')">#</th>
            <th scope="col" @click="sortByCol('name')">Name</th>
            <th scope="col" width="150px" @click="sortByCol('status')">
              ステータス
            </th>
            <th scope="col" width="150px" @click="sortByCol('dueDate')">
              期日
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="task in this.tasks"
            :key="task._id"
            draggable
            @dragstart="dragList($event, task.id)"
            @drop="dropList($event, task.id)"
            @dragover.prevent
            @dragenter.prevent
          >
            <th>
              <router-link :to="'/tickets/' + task.id">{{
                task.id
              }}</router-link>
            </th>
            <td>
              <router-link :to="'/tickets/' + task.id">
                {{ task.name }}
              </router-link>
            </td>
            <td>{{ task.status != "" ? task.status : "Open" }}</td>
            <td>{{ task.dueDate != "" ? task.dueDate : "-" }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
import Task from "@/modules/task";
import TaskNoteAPI from "@/modules/task_note_api";

export default {
  data() {
    return {
      togleSort: {
        id: -1,
        name: -1,
        status: -1,
        dueDate: -1,
      },
      timer: "",
      prevContent: "",
      content: "",
      dragIndex: 0,
    };
  },
  computed: {
    tasks() {
      return this.$store.state.tasks;
    },
  },
  created() {
    this.timer = setInterval(this.autosave, 5 * 1000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    sortByCol(key) {
      this.togleSort[key] = this.togleSort[key] * -1;
      this.tasks.sort(
        (a, b) => (a[key] < b[key] ? -1 : 1) * this.togleSort[key]
      );
    },
    autosave() {
      this.content = Task.toContent(this.$store.state.tasks);
      if (this.prevContent != this.content) {
        this.prevContent = this.content;
        TaskNoteAPI.callSave((response) => {
          console.log(response.data);
          document.getElementsByClassName("updated_timestamp")[0].textContent =
            "Last saved: " + new Date();
        }, this.content);
      } else {
        console.log("skip autosave");
      }
    },
    dragList(event, id) {
      let index = this.findById(id);
      this.dragIndex = index;
    },
    dropList(event, id) {
      let index = this.findById(id);
      this.moveTo(this.dragIndex, index, this.tasks);
    },
    findById(id) {
      let index = 0;
      for (let i = 0; i < this.tasks.length; i++) {
        if (this.tasks[i].id == id) {
          index = i;
        }
      }
      return index;
    },
    moveTo(index, to, array) {
      if (index === to || index > array.length - 1 || to > array.length - 1) {
        return array;
      }

      const value = array[index];
      const tail = array.slice(index + 1);

      array.splice(index);
      Array.prototype.push.apply(array, tail);
      array.splice(to, 0, value);

      return array;
    },
  },
};
</script>
