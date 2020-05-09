package files.models;

        import lombok.AllArgsConstructor;
        import lombok.Data;

        import java.util.List;
        import java.util.UUID;

@Data
@AllArgsConstructor
public class MetaData {
    private UUID uuid;
    private String fileName;
    private List<DataBlock> dataBlockList;
    private int size;
}
