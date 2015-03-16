package com.automation;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class CSVParser
{
    private static final String inputHtmlReport="./src/com/automation/report/extra/scriptReport.html";
    private static final String outputHtmlReport="./src/com/automation/report/rogersReport.html";
    private static StringBuffer indexContent = new StringBuffer();

    public static void main(String[] args) throws IOException {

        FileInputStream fis = null;
        FileOutputStream fos = null;

        //Input file which needs to be parsed
        String fileToParse = "C:/apache-jmeter-2.11/Results/Test.csv";
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            int serialNo = 1;
            String stringData =null;
            boolean flag=false;
            //Read the file line by line
            while ((line = fileReader.readLine()) != null)
            {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);

                String serialNumber = String.valueOf(serialNo);
                String scriptName = tokens[2];
                String responseCode = tokens[3];
                String responseMessage = tokens[4];
                String threadName = tokens[5];
                String status = tokens[7];
                String url = tokens[9];
                    if(serialNo==1 && !flag){
                       serialNo=0;
                        flag=true;
                    }else{
                        stringData = "<tr><td align='center'>" +serialNumber+"</td><td align='center'>" +scriptName+"</td>" +
                                "<td align='center'>" +responseCode+"</td><td align='left'>"+responseMessage+"</td>"+  "<td align='left'>" +threadName+
                                "<td align='center'>" +status+ "<td align='left'>" +url+"</tr> ";
                    }

                if(serialNo!=0){
                    indexContent.append(stringData);
                }
                serialNo++;
            }

            fis=new FileInputStream(inputHtmlReport);
            byte[] data=new byte [fis.available()];
            fis.read(data);
            fis.close();
            String all = new String(data);
            all=all.replace("MatrixExecutionSummary",indexContent);
            fos=new FileOutputStream(outputHtmlReport);
            fos.write(all.getBytes());
            fos.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //creating zip file of html reports and images
        String zipFile = "./src/com/automation/archiveReport/archive.zip";

        String srcDir = "./src/com/automation/report";

        try {

            FileOutputStream outputFileStram = new FileOutputStream(zipFile);

            ZipOutputStream zos = new ZipOutputStream(outputFileStram);

            File srcFile = new File(srcDir);

            addDirToArchive(zos, srcFile);

            // close the ZipOutputStream
            zos.close();

        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }
    }

    private static void addDirToArchive(ZipOutputStream zos, File srcFile) {
        File[] files = srcFile.listFiles();
        System.out.println("Adding directory: " + srcFile.getName());
        for (int i = 0; i < files.length; i++) {
            // if the file is directory, use recursion
            if (files[i].isDirectory()) {
                addDirToArchive(zos, files[i]);
                continue;
            }
            try {
                System.out.println("file: " + files[i].getName());
                // create byte buffer
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                // close the InputStream
                fis.close();
            } catch (IOException ioe) {
                System.out.println("IOException :" + ioe);
            }
        }
    }
}