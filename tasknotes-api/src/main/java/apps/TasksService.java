/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author koduki
 */
@Dependent
public class TasksService {

    @ConfigProperty(name = "tasknotes.gcp.project")
    String projectId;
    @ConfigProperty(name = "tasknotes.gcp.bucket.image")
    String imageBucket;
    @ConfigProperty(name = "tasknotes.gcp.bucket.data")
    String dataBucket;

    public Map<String, String> uploadImage(String userId, ImageFormBean image) {
        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(imageBucket, userId + "/" + UUID.randomUUID().toString() + ".png");
        var blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, image.getFile());
        var r = Map.of("url", "https://storage.googleapis.com/" + imageBucket + "/" + blobInfo.getName());
        return r;
    }

    public void saveNote(String userId, String note, TasksDocument document) {
        var md = document.getText();

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(dataBucket, userId + "/" + note + ".md");
        var blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, md.getBytes());
    }

    public Optional<TasksDocument> loadNote(String userId, String note) {
        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(dataBucket, userId + "/" + note + ".md");
        if (storage.get(blobId) != null) {
            var output = new ByteArrayOutputStream();
            storage.get(blobId).downloadTo(output);

            var md = new String(output.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
            return Optional.of(new TasksDocument(md));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Map<String, String>> listNote(String userId) {
        var data = Map.of(
                "tasks", "メイン",
                "todo", "TODO"
        );
        return Optional.of(data);
    }

    String findNoteKey(String noteName) {
        var notesName = Map.of("tasks", "メイン");
        var noteKey = notesName.entrySet().stream()
                .filter(x -> x.getValue().equals(noteName))
                .findFirst().get()
                .getKey();
        return noteKey;
    }
}
