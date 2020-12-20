import firebase from "firebase/app";
import "firebase/auth";
import store from "@/store";

export default {
  init() {
    const config = {
      apiKey: process.env.VUE_APP_AUTH_API_KEY,
      authDomain: process.env.VUE_APP_AUTH_API_DOMAIN,
    };
    firebase.initializeApp(config);
  },
  loginWithTwitter(callback) {
    this.login(new firebase.auth.TwitterAuthProvider(), callback);
  },
  loginWithGoogle(callback) {
    this.login(new firebase.auth.GoogleAuthProvider(), callback);
  },
  logout(callback) {
    firebase
      .auth()
      .signOut()
      .then(() => {
        store.dispatch("user/drop");
        callback();
      })
      .catch((error) => {
        console.error("Sign Out Error", error);
      });
  },
  login(provider, callback) {
    firebase
      .auth()
      .setPersistence(firebase.auth.Auth.Persistence.LOCAL)
      .then(() =>
        firebase
          .auth()
          .signInWithPopup(provider)
          .then((res) => {
            res.user
              .getIdToken()
              .then(function(token) {
                store.dispatch("user/store", {
                  id: res.user.uid,
                  token: token,
                  name: res.user.displayName,
                  pic: res.user.photoURL,
                });
                callback();
              })
              .catch((error) => {
                console.log(error);
                this.errorMessage = error.message;
                this.showError = true;
              });
          })
      )
      .catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
      });
  },
  user() {
    if (store.state.user.token != "" && firebase.auth().currentUser != null) {
      const current = new Date().getTime();
      const diff = (current - store.state.user.timestamp) / 1000;
      if (diff > 60) {
        console.log("reflesh");
        firebase
          .auth()
          .setPersistence(firebase.auth.Auth.Persistence.LOCAL)
          .then(() =>
            firebase
              .auth()
              .currentUser.getIdToken(true)
              .then(function(token) {
                store.dispatch("user/reflesh", token);
              })
              .catch(function(error) {
                console.log(error);
              })
          );
      }
    }
    return store.state.user;
  },
};
