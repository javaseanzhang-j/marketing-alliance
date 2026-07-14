package com.aiaffiliate.notion.registry;

import com.aiaffiliate.notion.exception.NotionIntegrationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** 将 Database ID 持久化到本地 JSON，且不修改 .env。 */
public class JsonDatabaseIdRegistry implements DatabaseIdRegistry {
    private final Path path; private final ObjectMapper mapper;
    public JsonDatabaseIdRegistry(Path path, ObjectMapper mapper) { this.path = path.toAbsolutePath().normalize(); this.mapper = mapper; }
    @Override public Map<String, String> load() {
        if (!Files.exists(path)) return new LinkedHashMap<>();
        try { return new LinkedHashMap<>(mapper.readValue(path.toFile(), new TypeReference<Map<String, String>>() {})); }
        catch (IOException e) { throw new NotionIntegrationException("Unable to read Notion database registry: " + path, 500, e); }
    }
    @Override public void save(Map<String, String> databaseIds) {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), new TreeMap<>(databaseIds));
            try { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ignored) { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING); }
        } catch (IOException e) { throw new NotionIntegrationException("Unable to persist Notion database registry: " + path, 500, e); }
    }
}
