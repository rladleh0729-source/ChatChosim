package com.chatchosim.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private static final String DATA_ROOT = "admin_data";
    private static final String RAW_DATA_DIR = DATA_ROOT + File.separator + "raw_datasets";
    private static final String CHECKPOINT_DIR = DATA_ROOT + File.separator + "checkpoints";
    private static final String LOG_DIR = DATA_ROOT + File.separator + "logs";

    public AdminApiController() {
        new File(RAW_DATA_DIR).mkdirs();
        new File(CHECKPOINT_DIR).mkdirs();
        new File(LOG_DIR).mkdirs();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("mode", "fully-generative-ai");
        result.put("serverTime", LocalDateTime.now().toString());
        result.put("rawDatasetCount", countFiles(RAW_DATA_DIR));
        result.put("checkpointCount", countFiles(CHECKPOINT_DIR));
        result.put("logCount", countFiles(LOG_DIR));
        result.put("activeModel", "ChatChosim_Generative_v0");
        result.put("trainingState", "idle");
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/upload-dataset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadDataset(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();

        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "업로드된 파일이 없다.");
            return ResponseEntity.badRequest().body(result);
        }

        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), "dataset.bin");
        String savedName = System.currentTimeMillis() + "_" + originalName;
        File target = new File(RAW_DATA_DIR, savedName);
        file.transferTo(target);

        result.put("success", true);
        result.put("message", "데이터셋 업로드 완료");
        result.put("savedPath", target.getAbsolutePath());
        result.put("fileName", savedName);
        result.put("size", file.getSize());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/datasets")
    public ResponseEntity<Map<String, Object>> listDatasets() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("items", listSimpleFiles(RAW_DATA_DIR));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkpoints")
    public ResponseEntity<Map<String, Object>> listCheckpoints() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("items", listSimpleFiles(CHECKPOINT_DIR));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> listLogs() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("items", listSimpleFiles(LOG_DIR));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/start-training")
    public ResponseEntity<Map<String, Object>> startTraining(@RequestBody(required = false) Map<String, Object> body) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();

        String modelName = body != null && body.get("modelName") != null
                ? String.valueOf(body.get("modelName"))
                : "ChatChosim_Generative_v0";

        String baseModel = body != null && body.get("baseModel") != null
                ? String.valueOf(body.get("baseModel"))
                : "small-transformer";

        int epochs = body != null && body.get("epochs") != null
                ? Integer.parseInt(String.valueOf(body.get("epochs")))
                : 3;

        int batchSize = body != null && body.get("batchSize") != null
                ? Integer.parseInt(String.valueOf(body.get("batchSize")))
                : 4;

        File fakeLog = new File(LOG_DIR, "train_" + System.currentTimeMillis() + ".log");
        java.nio.file.Files.writeString(
                fakeLog.toPath(),
                "TRAINING REQUEST\n"
                        + "time=" + LocalDateTime.now() + "\n"
                        + "modelName=" + modelName + "\n"
                        + "baseModel=" + baseModel + "\n"
                        + "epochs=" + epochs + "\n"
                        + "batchSize=" + batchSize + "\n"
                        + "status=queued\n"
        );

        result.put("success", true);
        result.put("message", "학습 작업이 등록되었다.");
        result.put("modelName", modelName);
        result.put("baseModel", baseModel);
        result.put("epochs", epochs);
        result.put("batchSize", batchSize);
        result.put("logFile", fakeLog.getName());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/activate-model")
    public ResponseEntity<Map<String, Object>> activateModel(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new LinkedHashMap<>();

        String checkpoint = body.get("checkpoint") == null ? "" : String.valueOf(body.get("checkpoint")).trim();

        if (checkpoint.isEmpty()) {
            result.put("success", false);
            result.put("message", "활성화할 체크포인트 이름이 비어 있다.");
            return ResponseEntity.badRequest().body(result);
        }

        result.put("success", true);
        result.put("message", "활성 모델 변경 완료");
        result.put("activeModel", checkpoint);
        return ResponseEntity.ok(result);
    }

    private int countFiles(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        return files == null ? 0 : files.length;
    }

    private List<Map<String, Object>> listSimpleFiles(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        List<Map<String, Object>> items = new ArrayList<>();

        if (files == null) {
            return items;
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File file : files) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("name", file.getName());
            row.put("size", file.length());
            row.put("lastModified", file.lastModified());
            row.put("directory", file.isDirectory());
            items.add(row);
        }

        return items;
    }
}