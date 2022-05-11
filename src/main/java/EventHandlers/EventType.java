package EventHandlers;

import java.io.File;

/**
 * A class to store the filename and the filetype. This assumes that the last "_" will be the
 * delimiter between the filename and the filetype.
 */
public class EventType {
    private final String fileName;
    private final String fileType;

    public EventType(File file){
        int splitIndex = file.getName().lastIndexOf("_");
        this.fileName = file.getName().substring(0, splitIndex);
        this.fileType = file.getName().substring(splitIndex+1);
    }
    public String getFileName() {
        return fileName;
    }
    public String getFileType() {
        return fileType;
    }
}
