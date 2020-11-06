import TaskHelper from "@/modules/task_helper";
import TaskNotesAPI from "@/modules/tasknotes_api";
import store from "@/store";
import Template from "@/modules/template";

export default class {
  constructor() {
    this.timer = null;
    this.callbackForSave = null;
    this.currentNote = "tasks";
    this.notes = {
      "tasks": {
        content:"",
        prevContent:""
      }
    }


    if (this.data.length === 0) {
      this.text = Template.defaultEditorContents();
    } else {
      this.notes[this.currentNote].content = TaskHelper.toContent(this.data);
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
      
      TaskNotesAPI.callLoad(this.currentNote, (response) => {
        this.text = response.data.text;
        callback();
      }, errorHandling);
    };

    this.list = (callback, errorHandling) => {    
      TaskNotesAPI.callNotes((response) => {
        // console.log(response.data)
        // this.notes = response.data;
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

      if (this.notes[this.currentNote].prevContent != this.notes[this.currentNote].content) {
        this.notes[this.currentNote].prevContent = this.notes[this.currentNote].content;
        TaskNotesAPI.callSave(this.currentNote, (response) => {
          if (this.callbackForSave != null) {
            this.callbackForSave(response.data);
          }
          console.log("save");
        }, this.notes[this.currentNote].content);
      } else {
        console.log("skip save");
      }
    };
  }

  get data() {
    return store.state.task.tasks;
  }
  set data(value) {
    store.dispatch("task/updateTasks", value);
    this.notes[this.currentNote].content = TaskHelper.toContent(value);
  }

  get text() {
    return this.notes[this.currentNote].content;
  }
  set text(value) {
    this.notes[this.currentNote].content = value;
    this.data = TaskHelper.parse(value);
  }
}
