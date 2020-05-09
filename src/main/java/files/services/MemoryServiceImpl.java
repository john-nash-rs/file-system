package files.services;

import files.models.DataBlock;

import java.util.ArrayList;
import java.util.List;

public class MemoryServiceImpl implements IMemoryService {
    int blockSize = 5;
    byte[] memory;
    int memorySize;
    List<DataBlock> dataBlocks;

    public MemoryServiceImpl(int memorySize) throws Exception {
        this.memory = new byte[memorySize];
        this.memorySize = memorySize;
        this.dataBlocks = new ArrayList<DataBlock>();
        createDataBlock();
    }

    public void createDataBlock() throws Exception {
        if(memorySize == 0){
            throw new RuntimeException("Memory Size given is 0");
        }
        int num = memorySize/blockSize;
        for(int i = 0; i < num; i++){
            DataBlock dataBlock = new DataBlock(blockSize * i,false);
            dataBlocks.add(dataBlock);
        }
    }

    public List<DataBlock> write(String fileData)  {
        List<DataBlock> dataBlockList = new ArrayList<DataBlock>();
        byte[] fileBytes = fileData.getBytes();
        int length = fileBytes.length;
        length = getDataBlockToWrite(dataBlockList, length);
        if(length > 0){
            throw new RuntimeException("Memory not available ");
        }
        int index = 0;
        int totalSize = fileBytes.length;
        for(DataBlock dataBlock : dataBlockList){
            dataBlock.setIsOccupied(true);
            for(int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++){
                if(index == totalSize){
                    break;
                }
                memory[i] = fileBytes[index];
                index = index + 1;
            }
        }
        return dataBlockList;
    }

    private int getDataBlockToWrite(List<DataBlock> dataBlockList, int length) {
        for (DataBlock dataBlock : dataBlocks) {
            if (length <= 0) {
                break;
            }
            if (dataBlock.getIsOccupied()) {
                continue;
            }
            dataBlockList.add(dataBlock);
            length = length - blockSize;
        }
        return length;
    }

    public String read(List<DataBlock> dataBlocks, int size) {
        byte[] fileBytes = new byte[size];
        int index = 0;
        for(DataBlock dataBlock : dataBlocks){
            for(int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++){
                if(index == size){
                    break;
                }
                fileBytes[index] = memory[i];
                index = index + 1;
            }
        }
        return new String(fileBytes);
    }

    public void delete(List<DataBlock> dataBlockList) {
        for(DataBlock dataBlock : dataBlocks){
            dataBlock.setIsOccupied(false);
        }
    }

    public List<DataBlock> update(List<DataBlock> dataBlockList, int oldFileSize, String newFileData)  {
        //oldFileBytes this is for rollback.
        byte[] oldFileBytes = new byte[oldFileSize];
        int numOfBlocks = dataBlockList.size();

        byte[] newFileBytes = newFileData.getBytes();
        int newFileLen = newFileData.getBytes().length;
        int remainingSize = newFileLen - numOfBlocks * blockSize;
        remainingSize = getMoreDataBlocksForUpdate(dataBlockList, remainingSize);
        if(remainingSize > 0){
            throw new RuntimeException("Memory not available for update");
        }
        int index = 0;
        try {
            for (DataBlock dataBlock : dataBlockList) {
                dataBlock.setIsOccupied(true);
                for (int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++) {
                    if (index == newFileLen) {
                        break;
                    }
                    if(index < oldFileSize) {
                        oldFileBytes[index] = memory[i];
                    }
                    memory[i] = newFileBytes[index];
                    index = index + 1;
                }
            }
        } catch (Exception e){
            System.out.println("Exception occured "+e.getMessage());
            e.printStackTrace();
            rollBack(index, oldFileSize, dataBlockList);
        }

        return dataBlockList;
    }

    private int getMoreDataBlocksForUpdate(List<DataBlock> dataBlockList, int remainingSize) {
        if(remainingSize > 0){
            for(DataBlock dataBlock : dataBlocks){
                if(remainingSize <= 0 ){
                    break;
                }
                if(dataBlock.getIsOccupied()){
                    continue;
                }
                dataBlockList.add(dataBlock);
                remainingSize = remainingSize - blockSize;
            }
        }
        return remainingSize;
    }

    private void rollBack(int i, int index, List<DataBlock> dataBlockList) {
    //
    }

}
