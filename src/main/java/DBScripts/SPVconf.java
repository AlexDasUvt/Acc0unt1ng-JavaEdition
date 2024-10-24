package DBScripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SPVconf {
    protected String catincPath;
    protected String catexpPath;
    protected String subcatPath;
    protected String currPath;

    public SPVconf() {
        this.catincPath = "src/main/DB/catinc.txt";
        this.catexpPath = "src/main/DB/catexp.txt";
        this.subcatPath = "src/main/DB/subcat.txt";
        this.currPath = "src/main/DB/curr.txt";
    }

    public void WriteSPV(String pth, String newSPV, String inpMode) {
        FileWriter fw = null;
        PrintWriter pw = null;
        newSPV = newSPV.replace(",", "\n");

        try {
            if (pth.equalsIgnoreCase("catinc")) {
                fw = new FileWriter(catincPath, true);
            } else if (pth.equalsIgnoreCase("catexp")) {
                fw = new FileWriter(catexpPath, true);
            } else if (pth.equalsIgnoreCase("subcat")) {
                fw = new FileWriter(subcatPath, true);
            } else if (pth.equalsIgnoreCase("curr")) {
                fw = new FileWriter(currPath, true);
            } else {
                throw new Exception("Invalid path: " + pth);
            }

            if (inpMode.equalsIgnoreCase("a")) {
                pw = new PrintWriter(fw, true);
            } else if (inpMode.equalsIgnoreCase("r")) {
                pw = new PrintWriter(fw);
            } else {
                throw new RuntimeException("Invalid input mode: " + inpMode);
            }

            pw.write(newSPV);
            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String ReadSPV(String pth) {
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        String line;

        try {
            if (pth.equalsIgnoreCase("catinc")) {
                br = new BufferedReader(new FileReader(catincPath));
            } else if (pth.equalsIgnoreCase("catexp")) {
                br = new BufferedReader(new FileReader(catexpPath));
            } else if (pth.equalsIgnoreCase("subcat")) {
                br = new BufferedReader(new FileReader(subcatPath));
            } else if (pth.equalsIgnoreCase("curr")) {
                br = new BufferedReader(new FileReader(currPath));
            } else {
                throw new Exception("Invalid path: " + pth);
            }

            // Read each line from the file and append it to the result
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }

            br.close();

            // Remove the last newline character if present
            if (!result.isEmpty()) {
                result.setLength(result.length() - 1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result.toString();
    }
}
