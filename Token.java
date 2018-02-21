/**
 * Token class for a SimpCalc compiler.
 * Token objects are generated by the ScannerModule class.
 *
 * @author JP Vergara
 * @version 1.0
 */

public class Token
{
    private int id;
    private String lexeme;

    /**
     * Constructor for Token objects.  Requires an id and a lexeme
     *
     * @param id    integer value from one of the Token constants defined at the end of this class
     * @param lexeme    actual string that corresponds to the token
     */
    public Token(int id, String lexeme)
    {
        this.id = id;
        this.lexeme = lexeme;
    }

    /**
     * Accessor method for token id
     * @return token id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Accessor method for token lexeme
     * @return token lexeme
     */
    public String getLexeme()
    {
        return lexeme;
    }

    // constants representing valid token ids
    public static final int Error = 0;
    public static final int Identifier = 1;
    public static final int Number     = 2;
    public static final int String     = 3;
    public static final int Assign     = 4;
    public static final int Semicolon  = 5;
    public static final int Colon      = 6;
    public static final int Comma      = 7;
    public static final int LeftParen  = 8;
    public static final int RightParen = 9;
    public static final int Plus       =10;
    public static final int Minus      =11;
    public static final int Multiply   =12;
    public static final int Divide     =13;
    public static final int Raise      =14;
    public static final int LessThan   =15;
    public static final int Equal      =16;
    public static final int GreaterThan=17;
    public static final int LTEqual    =18;
    public static final int NotEqual   =19;
    public static final int GTEqual    =20;
    public static final int EndOfFile  =21;
    public static final int Print      =22;
    public static final int If         =23;
    public static final int Else       =24;
    public static final int Endif      =25;
    public static final int Sqrt       =26;
    public static final int And        =27;
    public static final int Or         =28;
    public static final int Not        =29;

    // names of tokens for display (space padding ensures neat formatting)
    public static String[] tokenNames =
    {
       "Error      ",
       "Identifier ","Number     ","String     ","Assign     ","Semicolon  ",
       "Colon      ","Comma      ","LeftParen  ","RightParen ","Plus       ",
       "Minus      ","Multiply   ","Divide     ","Raise      ","LessThan   ",
       "Equal      ","GreaterThan","LTEqual    ","NotEqual   ","GTEqual    ",
       "EndOfFile  ","Print      ","If         ","Else       ","Endif      ",
       "Sqrt       ","And        ","Or         ","Not        "
    };

}
