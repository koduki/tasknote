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

    <table class="table" style="text-align=">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">ステータス</th>
          <th scope="col">期日</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="task in this.$store.state.tasks" :key="task._id">
          <th>
            <router-link :to="'/tickets/' + task.id">{{ task.id }}</router-link>
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
</template>
