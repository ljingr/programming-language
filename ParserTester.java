/**
 * ParserTester class that tests the ParserModule class by creating
 * a ParserModule object and calling its parse() method
 * 
 * @author JP Vergara 
 * @version 1.0
 */
public class ParserTester
{
    /**
     * Driver method that creates a ParserModule object and calls the parse() method
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
        ParserModule pm = new ParserModule(fileName);
        boolean valid = pm.parse();
        if (valid)
        {
           System.out.println(fileName +" is a valid SimpCalc program");
        }
    }

}