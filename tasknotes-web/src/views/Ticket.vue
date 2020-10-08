<template>
  <div class="container" style="text-align: left">
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

    <h2 class="h4">Details:</h2>
    <table class="table">
      <tbody>
        <tr>
          <th scope="col">ステータス</th>
          <td>{{ task.status != "" ? task.status : "Open" }}</td>
          <th scope="col">期日</th>
          <td>{{ task.dueDate != "" ? task.dueDate : "-" }}</td>
        </tr>
      </tbody>
    </table>
    <h2 class="h4">Description & Activity:</h2>
    <main role="main" class="col-md-12">
      <div>
        <vue-simplemde v-model="task.body" ref="markdownEditor" />
      </div>
    </main>
  </div>
</template>

<script>
import VueSimplemde from "vue-simplemde";

export default {
  name: "Ticket",
  components: {
    VueSimplemde,
  },
  data() {
    return {
      content: "",
      task: {},
    };
  },

  created() {
    this.$store.state.tasks;
    this.$route.params.id;
    this.$store.state.tasks.forEach((t) => {
      if (t.id == this.$route.params.id) {
        this.task = t;
      }
    });
  },

  mounted() {
    this.$refs.markdownEditor.simplemde.togglePreview();
  },
  methods: {},
};
</script>