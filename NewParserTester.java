/**
 * NewParserTester class that tests the NewParserModule class by creating
 * a NewParserModule object and calling its parse() method
 *
 * @author JP Vergara, Jingran Li
 * @version 1.0 - revised based on ParserTester.java
 */
public class NewParserTester
{
    /**
     * Driver method that creates a NewParserModule object and calls the parse() method
     *
     * @param args optional command line string argument (args[0]) to specify name of text file
     */
    public static void main(String[] args)
    {
        String fileName = "sample3.txt";
        if (args.length >= 1)
        {
            fileName = args[0];
        }
        NewParserModule pm = new NewParserModule(fileName);
        boolean valid = pm.parse();
        if (valid)
        {
           System.out.println(fileName +" is a valid SimpCalc program");
        }
    }

}
