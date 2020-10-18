import Auth from "@/modules/auth";
import axios from "axios";

export default {
  callUploadImage(file, handler, errorHandler) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/tasks/image";

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
  callLoad(callback) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/tasks/load";

    const config = {
      headers: {
        Authorization: "Bearer " + Auth.user().token,
      },
    };
    axios.get(uri, config).then((response) => {
      callback(response);
    });
  },
  callSave(callback, content) {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/tasks/save";
    const data = { text: content };

    const config = {
      headers: {
        Authorization: "Bearer " +  Auth.user().token,
      },
    };
    axios.post(uri, data, config).then((response) => {
      callback(response);
    });
  },
};
