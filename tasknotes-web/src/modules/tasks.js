import TaskHelper from "@/modules/task_helper";
import TaskNotesAPI from "@/modules/tasknotes_api";
import store from "@/store";
import Template from "@/modules/template";

export default class {
  constructor() {
    this.timer = null;
    this.callbackForSave = null;
    this.notes = {
      tasks: {
        content: "",
        prevContent: "",
      },
    };

    if (this.data.length === 0) {
      this.text = Template.defaultEditorContents();
    } else {
      this.note.content = TaskHelper.toContent(this.data);
    }

    //
    // Method Define
    //
    this.startAutoSave = (callbackForSave) => {
      this.callbackForSave = callbackForSave;
      this.timer = setInterval(this.save, 5 * 1000);
    };

    this.stopAutoSave = () => {
      clearInterval(this.timer);
    };

    this.load = (callback, errorHandling) => {
      TaskNotesAPI.callLoad(
        this.current,
        (response) => {
          this.text = response.data.text;
          callback();
        },
        errorHandling
      );
    };

    this.list = (callback, errorHandling) => {
      TaskNotesAPI.callNotes((response) => {
        callback(response.data);
      }, errorHandling);
    };

    this.save = (task) => {
      if (task) {
        let xs = this.data;
        for (let i = 0; i < xs.lentgh; i++) {
          if (xs[i].id == task.id) {
            xs[i] = t;
          }
        }
        this.data = xs;
      }

      if (this.note.prevContent != this.note.content) {
        this.note.prevContent = this.note.content;
        TaskNotesAPI.callSave(
          this.current,
          (response) => {
            if (this.callbackForSave != null) {
              this.callbackForSave(response.data);
            }
            console.log("save");
          },
          this.note.content
        );
      } else {
        console.log("skip save");
      }
    };
  }

  get note() {
    if (!this.notes[this.current]) {
      this.notes[this.current] = {};
      this.notes[this.current].content = "";
      this.notes[this.current].prevContent = "";
    }
    return this.notes[this.current];
  }

  get current() {
    return store.state.task.currentNote;
  }
  set current(value) {
    console.log("001:current:value=" + value);
    store.dispatch("task/updateCurrent", value);
  }

  get data() {
    if (!store.state.task.notes[this.current]) {
      store.state.task.notes[this.current] = [];
    }
    return store.state.task.notes[this.current];
  }
  set data(value) {
    store.dispatch("task/updateTasks", value);
    this.note.content = TaskHelper.toContent(value);
  }

  get text() {
    console.log("002:text:data=" + this.data[0].name);
    this.note.content = TaskHelper.toContent(this.data);
    // console.log("003:text:content=" + this.note.content);
    return this.note.content;
  }
  set text(value) {
    this.note.content = value;
    this.data = TaskHelper.parse(value);
  }
}
