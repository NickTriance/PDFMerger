import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

public class PDFMerger {
    
    /**
     * Merges all the files in the array into one, and saves it to disk
     * @param files : String[], array of files to merge together
     * @param saveFile : String,  path to save the merged file to
     * @throws IOException
     */
    public static void merge(String[] files, String saveFile) throws IOException {
        
        PDFMergerUtility merger = new PDFMergerUtility(); //create the merger

        //add files to be merged
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            merger.addSource(file);
        }

        //save and merge
        merger.setDestinationFileName(saveFile);
        merger.mergeDocuments(null);
    }
}
