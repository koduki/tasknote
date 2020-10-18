<template>
  <div>
    <section class="pt-4 pt-md-11">
      <!-- Main -->
      <div class="container">
        <div class="row align-items-center">
          <div>
            <!-- Heading -->
            <h1 class="display-4 text-center text-md-left">
              タスクはもっと<span class="text-primary">気軽</span>に作れても良い
            </h1>

            <!-- Text -->
            <div class="lead text-center text-md-left text-muted mb-6 mb-lg-8">
              <p>
                JIRAやREDMINEのようなチケット管理は便利ですが、ちょっとタスクを作るのに気構えちゃいますよね。
              </p>
              <p>
                task.NotesはMarkdownで作ったメモを簡単にチケット風のタスク管理できるツールです。<br />
                メモとタスクの境目を気軽に超えていきましょう！
              </p>
            </div>
          </div>
        </div>
        <!-- / .row -->
      </div>
      <!-- / .container -->
    </section>

    <!-- Login -->
    <section class="py-8 py-md-11 border-bottom login">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <!-- Buttons -->
            <div class="text-center text-md-left">
              <h2>SNS認証でログインする</h2>

              <button type="button" class="btn" @click="signInWithGoogle">
                <img
                  src="/btn_google_signin_dark_normal_web@2x.png"
                  alt="Googleでログイン"
                />
              </button>
              <button type="button" class="btn" @click="signInWithTwitter">
                <img src="/Twitter.png" alt="Twitterでログイン" />
              </button>
            </div>
          </div>
        </div>
        <!-- / .row -->
      </div>
      <!-- / .container -->
    </section>

    <!-- Features -->
    <section class="py-8 py-md-11 border-bottom login">
      <div>
        <h3>編集モード (Mardown)</h3>
        <img src="/ss001.png" width="800px" />
      </div>
      <div>
        <h3>Ticketモード (一覧)</h3>
        <img src="/ss002.png" width="800px" />
      </div>
      <!-- / .container -->
    </section>

    <footer role="contentinfo" class="py-6 lh-1 bg-white">
      <div class="container">
        <div class="row">
          <div class="col-12 text-center text-sm">
            <p class="mb-0">Powered by なからぼ</p>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.login {
  margin: 50px;
}
.feature {
  padding-bottom: 50px;
}
footer {
  margin: 50px;
}
</style>

<script>
import Auth from "@/modules/auth";
export default {
  name: "Login",
  data() {
    return {};
  },
  methods: {
    signInWithGoogle: function () {
      const self = this;
      Auth.loginWithGoogle(() => self.$router.push("home"));
    },
    signInWithTwitter: function () {
      const self = this;
      Auth.loginWithTwitter(() => self.$router.push("home"));
    },
  },
};
</script>