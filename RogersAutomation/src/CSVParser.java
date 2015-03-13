import java.io.*;


public class CSVParser
{
    private static final String inputHtmlReport="./report/scriptReport.html";
    private static final String outputHtmlReport="./report/rogersReport.html";
    private static StringBuffer indexContent = new StringBuffer();

    public static void main(String[] args)
    {

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
    }
}