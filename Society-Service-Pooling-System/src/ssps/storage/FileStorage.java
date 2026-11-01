package ssps.storage;
import ssps.model.AppData;
import java.io.*;
import java.nio.file.*;
public class FileStorage {
    private final Path file = Path.of("data", "ssps.dat");
    public AppData load() throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) return new AppData();
        // Never substitute untrusted or downloaded serialized files.
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return (AppData) in.readObject();
        }
    }
    public void save(AppData data) throws IOException {
        Files.createDirectories(file.getParent());
        Path temp = file.resolveSibling("ssps.tmp");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temp))) { out.writeObject(data); }
        try { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
        catch (AtomicMoveNotSupportedException ex) { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING); }
    }
    public byte[] snapshot(AppData data) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(data); }
        return bytes.toByteArray();
    }
    public AppData restore(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) { return (AppData) in.readObject(); }
    }
}
