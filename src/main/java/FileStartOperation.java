import files.models.DataBlock;
import files.models.MetaData;
import files.services.MemoryServiceImpl;
import files.services.FileServiceImpl;
import files.services.IMemoryService;
import files.services.IFileService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileStartOperation {
    private static final int MEMORY_SIZE = 100;
    private static IMemoryService memoryService;
    private static List<MetaData> metaDataList = Collections.synchronizedList(new ArrayList<MetaData>());

    static {
        try {
            memoryService = new MemoryServiceImpl(MEMORY_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IFileService fileService = new FileServiceImpl(memoryService);


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        createFile(sc, "hello.txt","qwe");
        readFile(sc, "hello.txt");
        updateFile(sc, "hello.txt","12345678");
        readFile(sc, "hello.txt");
    }

    private static void fileOperation() {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Enter File Operation");
            int operation = sc.nextInt(); //take input from the user

            switch (operation){
                case 0:
                    //create
                    createFile(sc, "hello.txt","qwertyuiop");
                    break;
                case 1:
                    //read
                    readFile(sc, "hello.txt");
                    break;
                case 2:
                    //delete
                    deleteFile(sc);
                    break;
                case 3:
                    //update
                    //updateFile(sc);
                    break;

            }
        }
    }

    private static void updateFile(Scanner sc, String fileNameToRead, String newFileData) {
//        System.out.println("Enter File name");
//        String fileNameToRead = sc.nextLine();
//        System.out.println("Enter File data");
//        String newFileData = sc.nextLine();
        //Assuming all the file he/she is searching
        //to handle multiple , display the file names, make user select one and then delete that
        for(MetaData metaData : metaDataList){
            if(!metaData.getFileName().equals(fileNameToRead)){
                continue;
            }
            List<DataBlock> dataBlocks = fileService.update(metaData.getDataBlockList(), metaData.getSize(), newFileData);
            metaData.setDataBlockList(dataBlocks);
            metaData.setSize(newFileData.getBytes().length);
            break;
        }
    }

    private static void deleteFile(Scanner sc) {
        System.out.println("Enter File name");
        String fileNameToRead = sc.nextLine();
        //Assuming all the file he/she is searching
        //to handle multiple , display the file names, make user select one and then delete that
        for(MetaData metaData : metaDataList){
            if(!metaData.getFileName().equals(fileNameToRead)){
                continue;
            }
            fileService.delete(metaData.getDataBlockList());
        }
    }

    private static void readFile(Scanner sc, String fileNameToRead) {
        //ystem.out.println("Enter File name");
        //String fileNameToRead = sc.nextLine();
        //Assuming all the file he/she is searching
        //to handle multiple , display the file names, make user select one and then read that
        for(MetaData metaData : metaDataList){
            if(!metaData.getFileName().equals(fileNameToRead)){
                continue;
            }
            String fileData = fileService.read(metaData.getDataBlockList(), metaData.getSize());
            System.out.println("--"+fileData);
        }
    }

    private static void createFile(Scanner sc, String fileName, String fileData) {
//        System.out.println("Enter File name");
//        String fileName = sc.nextLine();
//        System.out.println("Enter File data");
//        String fileData = sc.nextLine();
        List<DataBlock> dataBlocks = fileService.create(fileName, fileData);
        MetaData metaData = new MetaData(UUID.randomUUID(), fileName, dataBlocks, fileData.getBytes().length);
        metaDataList.add(metaData);
    }
}
