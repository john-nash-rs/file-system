package files.services;

import files.models.DataBlock;

import java.util.List;

public class FileServiceImpl implements IFileService {
    private IMemoryService memoryService;
    public FileServiceImpl(IMemoryService memoryService) {
        this.memoryService = memoryService;
    }

    public List<DataBlock> create(String fileName, String fileData) {
        //write wrapper to encrypt
        List<DataBlock> dataBlocks =  memoryService.write(fileData);
        return dataBlocks;
    }

    public String read(List<DataBlock> dataBlocks, int size) {
        //write wrapper to decrypt
        String fileData = memoryService.read(dataBlocks, size);
        return fileData;
    }

    public void delete(List<DataBlock> dataBlockList) {
        memoryService.delete(dataBlockList);
    }

    public List<DataBlock> update(List<DataBlock> dataBlockList, int oldFileSize, String newFileData) {
        List<DataBlock> dataBlocks =  memoryService.update(dataBlockList, oldFileSize, newFileData);
        return dataBlocks;
    }

}
