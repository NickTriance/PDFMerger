//https://www.geeksforgeeks.org/merging-pdfs-using-java/

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PDFMerger {
    
    public static void merge(String[] files, String saveFile) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();

        //todo: add all sources to the object. probably best to take in an array and use a loop
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            merger.addSource(file);
        }

        merger.setDestinationFileName(saveFile);
        merger.mergeDocuments(null);
    }
}
