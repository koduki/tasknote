/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author koduki
 */
@Dependent
public class TaskService {

    @ConfigProperty(name = "todo.gcp.project")
    String projectId;

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            var credentials = GoogleCredentials.getApplicationDefault();
            var options = new FirebaseOptions.Builder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    public List<Task> findTasks() throws ExecutionException, InterruptedException {
        var db = FirestoreClient.getFirestore();
      
        return db.collection("tasks").get().get().getDocuments().stream()
                .map(x -> toTask(x))
                .collect(Collectors.toList());
    }

    public List<Task> findNewItems(int count) throws ExecutionException, InterruptedException {
        var db = FirestoreClient.getFirestore();
        var query = db.collection("items")
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .limit(count)
                .get();
        return query.get().getDocuments().stream()
                .map(x -> toTask(x))
                .collect(Collectors.toList());

    }

    public Task getItem(String id) throws InterruptedException, ExecutionException {
        var db = FirestoreClient.getFirestore();
        var item = db.document("items/" + id).get().get();
        return toTask(item);
    }

    public List<Tag> getTags() throws InterruptedException, ExecutionException {
        var db = FirestoreClient.getFirestore();

        var query = db.collection("tags").get();
        var tags = query.get().getDocuments().stream().map(r -> new Tag(r.getId(), r.getString("name"))).collect(Collectors.toList());
        return tags;
    }

//    public ApiFuture<WriteResult> updateItem(Task item) throws InterruptedException, ExecutionException {
//        var db = FirestoreClient.getFirestore();
//        var tagList = persistTags(item.tags, db);
//        var doc = Map.of(
//                "name", item.name,
//                "url", item.url,
//                "type", item.type,
//                "tags", tagList.stream().collect(Collectors.toMap(x -> x, x -> true)),
//                "description", item.description,
//                "details", item.details,
//                "updated_at", (item.updatedAt == null) ? new Date() : item.updatedAt
//        );
//        return db.collection("items").document(item.id).set(doc);
//    }
    public ApiFuture<WriteResult> createTask(Task task) throws InterruptedException, ExecutionException {
        var db = FirestoreClient.getFirestore();

        var doc = Map.of(
                "name", task.name,
                "body", task.body
        );
        return db.collection("tasks").document().set(doc);
    }

    List<String> persistTags(String tags, Firestore db) throws ExecutionException, InterruptedException {
        var tagList = Stream.of(tags.trim().split(","))
                .map(s -> s.trim().toLowerCase())
                .filter(s -> !s.trim().isBlank())
                .collect(Collectors.toList());

        for (var tag : tagList) {
            db.collection("tags").document(tag).set(Map.of("name", tag, "priority", 0)).get();
        }
        return tagList;
    }

    ApiFuture<QuerySnapshot> findByContainKey(CollectionReference col, String property, Set<String> keys) {
        Query query = null;
        for (var key : keys) {
            var name = property + "." + key;
            query = (query == null) ? col.whereEqualTo(name, true) : query.whereEqualTo(name, true);
        }

        return query.get();
    }

    Task toTask(DocumentSnapshot task) {
        return new Task(
                task.getId(),
                task.getString("name"),
                task.getString("body")
        );
    }
}
