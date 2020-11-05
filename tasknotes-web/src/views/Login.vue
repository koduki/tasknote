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
                  width="80%"
                  src="/btn_google_signin_dark_normal_web@2x.png"
                  alt="Googleでログイン"
                />
              </button>
              <button type="button" class="btn" @click="signInWithTwitter">
                <img width="80%" src="/Twitter.png" alt="Twitterでログイン" />
              </button>
            </div>
          </div>
        </div>
        <!-- / .row -->
      </div>
      <!-- / .container -->
    </section>

    <!-- Features -->
    <section>
      <video
        width="720px"
        height="480px"
        style="object-fit: fill"
        controls
        muted
        autoplay
        playinline
        loop
        src="https://storage.googleapis.com/tasknotes-image-a1216tfewrf3/taskntoes-ss.mp4"
      >
        <p>動画を再生するにはvideoタグをサポートしたブラウザが必要です。</p>
      </video>
    </section>

    <!-- Features -->
    <section style="margin-top: 50px">
      <div class="container text-md-left">
        <h2 class="display-5">使い方</h2>
        <div>
          <p>通常のMarkdownエディタ。基本的な機能は以下の通り</p>
          <ul>
            <li><strong>#</strong>によるH1要素をタスクのタイトルとして判定</li>
            <li>
              マジックキーワード<strong>## meta</strong> 以下に<strong
                >- status:</strong
              >と<strong>- due_date:</strong>を記述できる。これらは省略可
            </li>
            <li>
              マジックキーワード <strong>## body</strong>以降を本文として認識
            </li>
            <li>画像を貼付けると自動アップロード</li>
            <li>コンテンツは定期的に自動保存される</li>
            <li>
              チケットモードでは並べ替えや個別チケットの編集、及びステータスの変更などが出来る
            </li>
          </ul>
          <strong>サンプル:</strong>
          <pre style="background-color: lightgray; padding: 10px; width: 900px">
# ここにタスク名
## meta
- due_date:2020-12-31
- status:Closed

## body
### ここにコンテンツ。
![image.png](https://storage.googleapis.com/ajfgeay8733/image/bb0c1b12-b2bc-4443-bbe3-9dd12870a2c1.png)

# タスク２

## body
* アイテム１

# タスク3
## meta
- due_date:2020/12/31

## body
ここにコンテンツ。
          </pre>
        </div>
      </div>
    </section>

    <footer role="contentinfo" class="py-6 lh-1 bg-white">
      <div class="container">
        <div class="row">
          <div class="col-12 text-center text-sm">
            <p class="mb-0">Powered by NKLAB</p>
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
import axios from "axios";
import Auth from "@/modules/auth";
export default {
  name: "Login",
  data() {
    return {};
  },
  created() {
    const host = process.env.VUE_APP_API_BASE_URL;
    const uri = (host == "none" ? "" : host) + "/healthcheck";

    axios
      .get(uri)
      .then((response) => {
        console.log("healthcheck:" + response.data);
      })
      .catch((error) => {
        console(
          `status: ${error.response.status}, message: ${error.response.data}`
        );
      });
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