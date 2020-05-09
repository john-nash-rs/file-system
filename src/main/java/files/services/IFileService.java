package files.services;

import files.models.DataBlock;

import java.util.List;

public interface IFileService {
    List<DataBlock> create(String fileName, String fileData);
    String read(List<DataBlock> dataBlocks, int size);
    void delete(List<DataBlock> dataBlockList);
    List<DataBlock> update(List<DataBlock> dataBlockList, int oldFileSize, String newFileData);
}
