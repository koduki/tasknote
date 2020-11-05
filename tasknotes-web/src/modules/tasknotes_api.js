import Auth from "@/modules/auth";
import axios from "axios";

export default {
  callUploadImage(file, handler, errorHandler) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/image";

    const data = new FormData();
    data.append("file", file);
    const config = {
      headers: {
        Authorization: "Bearer " + Auth.user().token,
      },
    };
    axios
      .post(uri, data, config)
      .then((response) => {
        handler(response);
      })
      .catch((error) => {
        errorHandler(error);
        console(
          `status: ${error.response.status}, message: ${error.response.data}`
        );
      });
  },
  callLoad(note, callback, errorHandling) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/notes/" + note + "/load";

    const config = {
      headers: {
        Authorization: "Bearer " + Auth.user().token,
      },
    };
    axios
      .get(uri, config)
      .then((response) => {
        callback(response);
      })
      .catch((error) => {
        if (error.response.status === 404) {
          console.log("initial load");
          errorHandling();
        } else {
          console.log(error.response.status);
        }
      });
  },
  callSave(note, callback, content) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/notes/" + note + "/save";
    const data = { text: content };

    const config = {
      headers: {
        Authorization: "Bearer " + Auth.user().token,
      },
    };
    axios.post(uri, data, config).then((response) => {
      callback(response);
    });
  },
  callNotes(callback) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/notes";

    const config = {
      headers: {
        Authorization: "Bearer " + Auth.user().token,
      },
    };
    axios.get(uri, config).then((response) => {
      callback(response);
    });
  },
};
