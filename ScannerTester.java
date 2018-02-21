import java.io.FileNotFoundException;
/**
 * ScannerTester class that tests the ScannerModule class by creating
 * a ScannerModule object and repeatedly calling its getToken() method
 *
 * @author JP Vergara
 * @version 1.0
 */
public class ScannerTester
{
    /**
     * Driver method that tests the ScannerModule class
     *
     * @param args optional command line string argument (args[0]) to specify name of text file
     */
    public static void main(String[] args)
    {
        String fileName = "sample1.txt";
        if (args.length >= 1)
        {
            fileName = args[0];
        }
        ScannerModule sm = new ScannerModule(fileName);
        Token t = sm.getToken();
        while ( t.getId() != Token.EndOfFile )
        {
            System.out.println(Token.tokenNames[t.getId()]+" "+t.getLexeme());
            t = sm.getToken();
        }
    }
}
