export default {
  toContent(tasks) {
    return tasks
      .map((t) => {
        let name = "# " + t.name;
        let meta =
          t.dueDate != "" || t.status != ""
            ? "\n## meta\n" +
              "- due_date:" +
              t.dueDate +
              "\n" +
              "- status:" +
              t.status
            : "";
        let body = "\n## body\n" + t.body;
        return name + "\n" + meta + "\n" + body.replace(/\n$/, "");
      })
      .join("\n")
      .replace(/\n\n+/g, "\n\n");
  },
  parse(content) {
    let lines = content.toString().split("\n");
    let tasks = [];
    let name = "";
    let isMeta = false;
    let dueDate = "";
    let status = "";
    let body = null;
    let id = 0;
    lines.forEach((line) => {
      // console.log(line);

      if (line.trim().match("^# ")) {
        if (body != null) {
          id++;
          let task = {
            id: id,
            name: name,
            dueDate: dueDate,
            status: status,
            body: body,
          };
          tasks.push(task);
        }
        name = line.trim().replace(/^# /, "");
        body = null;
        dueDate = "";
        status = "";
      } else if (line.trim().match("^## meta$")) {
        isMeta = true;
      } else if (
        isMeta &&
        (line.trim().match("^- due_date:") ||
          line.trim().match("^\\* due_date:"))
      ) {
        dueDate = line.split("due_date:")[1].trim();
      } else if (
        isMeta &&
        (line.trim().match("^- status:") || line.trim().match("^\\* status:"))
      ) {
        status = line.split("status:")[1].trim();
      } else if (line.trim().match("^## body$")) {
        isMeta = false;
        body = "";
      } else if (body != null) {
        body += line + "\n";
      }
    });
    id++;
    tasks.push({
      id: id,
      name: name,
      dueDate: dueDate,
      status: status,
      body: body,
    });

    return tasks;
  },
};
