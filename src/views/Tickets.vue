<template>
  <div class="about" style="text-align: left">
    <div class="align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 class="h2">チケットモード</h1>
    </div>

    <table class="table" style="text-align=">
      <thead>
        <tr>
          <th scope="col">Name</th>
          <th scope="col">ステータス</th>
          <th scope="col">期日</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="task in this.$store.state.tasks" :key="task._id">
          <td><a href=".">{{ task.name }}</a></td>
          <td>{{ (task.status != "") ? task.status : "Open"}}</td>
          <td>{{ (task.dueDate != "") ? task.dueDate : "-" }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
