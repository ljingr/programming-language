/**
 * ParserModule parses a SimpCalc program and determines if it follows
 * syntax rules.  Requires the Token class and ScannerModule class.
 * A ScannerModule object generates Token objects through a getToken() method.
 * Driver code would call parse() on a ParserModule object.
 * 
 * @author JP Vergara 
 * @version 1.0
 */
public class ParserModule
{    
    protected ScannerModule sm;           // the scanner module object from which getToken() is repeatedly called
    protected Token currentToken = null;  // current token being processed
    protected boolean errorFlag = false;  // set to true when an error occurs
        
    /**
     * Constructor for ParserModule objects.  Prepares the parsing activity
     * by creating a scanner module object
     *
     * @param filename specifies name of text file
     */
    public ParserModule(String filename) 
    {
        sm = new ScannerModule(filename);
    }
    
    /**
     * Gets next token and stores it in currentToken variable,
     * effectively consuming the previous token
     */    
    protected void getNextToken()
    {
        currentToken = sm.getToken();
    }
    
    /**
     * Checks if currentToken matches expected token.  Consumes token
     * and gets the next one if so, reports a parse error otherwise
     *
     * @param tokenId id of token to be matched
     */    
    protected void match(int tokenId)
    {
        if (currentToken.getId() == tokenId)
        {
            // ok, move to next token
            getNextToken();
        }
        else
        {
            parseError(Token.tokenNames[tokenId]+" expected.");
            System.out.println(Token.tokenNames[currentToken.getId()]+", but expected "+Token.tokenNames[tokenId]);
        }
    }

    /**
     * The key method of this class.  Begins the actual parse of the input program.
     * The first token is read and the subroutine for the start symbol of the grammar (Prg)
     * is called, initiating a recursive descent parse.
     *
     * @return true if no errors were encountered during the parse, false otherwise.
     */       
    public boolean parse()
    {
        errorFlag = false;
        getNextToken();
        Prg();
        return !errorFlag;
    }
    
    /**
     * Called whenever a parse error is encountered.  Normally, for more elegant error recovery,
     * control is brought back to the parser so it can validate next constructs.
     * But for this assignment, we just abort.
     *
     * @param errMessage string containing error message
     */       
    protected void parseError(String errMessage)
    {
            errorFlag = true;
            System.out.println("Parse Error: " + errMessage + " (line #" + sm.getLineNumber() + ")");
            System.exit(0);
    }
    
    /** 
     * The remaining methods constitute the recursive descent parser that corresponds to the grammar:
     * 
     * Prg -> Blk eof
     * Blk -> Stm Blk | eps
     * Stm -> identifier := Exp ; | print ( Arg Argfollow ); | if Cnd : Blk Iffollow
     * Argfollow -> , Arg Argfollow | eps
     * Arg -> string | Exp
     * Iffollow -> endif ; | else Blk endif ;
     * Exp -> Trm Trmfollow
     * Trmfollow -> + Trm Trfollow | - Trm Trfollow | eps
     * Trm -> Fac Facfollow
     * Facfollow -> * Fac Facfollow |/ Fac Facfollow | eps
     * Fac -> Lit Litfollow
     * Litfollow -> **Lit LitFollow | eps
     * Lit -> -Val |Val
     * Val -> identifier | number | sqrt(Exp) | (Exp)
     * Cnd -> Exp Rel Exp
     * Rel -> < | = | > | <= | != | >= 
     *
     */
    
    // Prg -> Blk eof
    protected void Prg()
    {
        Blk();
        match(Token.EndOfFile);
    }
    
    // Blk -> Stm Blk | eps
    protected void Blk()
    {
        // check for FIRST(Stm)
        if (currentToken.getId() == Token.Identifier ||
            currentToken.getId() == Token.Print ||
            currentToken.getId() == Token.If
           )
        {
            Stm();
            Blk();
        }
        else
        {
            // do nothing (epsilon production)
        }
    }
    
    // Stm -> identifier := Exp ; | print ( Arg Argfollow ); | if Cnd : Blk Iffollow
    protected void Stm()
    {
        switch(currentToken.getId())
        {
            case Token.Identifier:
               match(Token.Identifier);
               match(Token.Assign);
               Exp();
               match(Token.Semicolon);
               System.out.println("Assignment Statement Recognized");
               break;
            case Token.Print:
               match(Token.Print);
               match(Token.LeftParen);
               Arg();
               Argfollow();
               match(Token.RightParen);
               match(Token.Semicolon);
               System.out.println("Print Statement Recognized");
               break;
            case Token.If:
               match(Token.If);
               System.out.println("If Statement Begins");
               Cnd();
               match(Token.Colon);
               Blk();
               Iffollow();
               System.out.println("If Statement Ends");
               break;
            default:
               parseError("Invalid Statement");     
        }
    }
    
    // Argfollow -> , Arg Argfollow | eps
    protected void Argfollow()
    {
        if (currentToken.getId() == Token.Comma)
        {
            match(Token.Comma);
            Arg();
            Argfollow();
        }
        else
        {
            // do nothing (epsilon production)
        }
    }
    
    // Arg -> string | Exp
    protected void Arg()
    {
        if (currentToken.getId() == Token.String)
        {
            match(Token.String);
        }
        else
        {
            Exp();
        }
        
    }

    // Iffollow -> endif ; | else Blk endif ;
    protected void Iffollow()
    {
        if (currentToken.getId() == Token.Endif)
        {
            match(Token.Endif);
            match(Token.Semicolon);
        }
        else if (currentToken.getId() == Token.Else)
        {
            match(Token.Else);
            Blk();
            match(Token.Endif);
            match(Token.Semicolon);            
        }
        else
        {
            parseError("Incomplete if statement");
        }
    }

    // Exp -> Trm Trmfollow
    protected void Exp()
    {
        Trm();
        Trmfollow();
    }
    
    // Trfollow -> + Trm Trfollow | - Trm Trfollow | eps
    protected void Trmfollow()
    {
        if (currentToken.getId() == Token.Plus)
        {
            match(Token.Plus);
            Trm();
            Trmfollow();
        }
        else if (currentToken.getId() == Token.Minus)
        {
            match(Token.Minus);
            Trm();
            Trmfollow();
        }
        else
        {
            // do nothing (epsilon production)
        }
    }

    // Trm -> Fac Facfollow
    protected void Trm()
    {
        Fac();
        Facfollow();
    }
    
    // Facfollow -> * Fac Facfollow | / Fac Facfollow | eps
    protected void Facfollow()
    {
        if (currentToken.getId() == Token.Multiply)
        {
            match(Token.Multiply);
            Fac();
            Facfollow();
        }
        else if (currentToken.getId() == Token.Divide)
        {
            match(Token.Divide);
            Fac();
            Facfollow();
        }
        else
        {
            // do nothing (epsilon production)
        }
    }

    // Fac -> Lit Litfollow
    protected void Fac()
    {
        Lit();
        Litfollow();
    }
    
    // Litfollow -> **Lit Litfollow | eps
    protected void Litfollow()
    {
        if (currentToken.getId() == Token.Raise)
        {
            match(Token.Raise);
            Lit();
            Litfollow();
        }
        else
        {
            // do nothing (epsilon production)
        }
    }
    
    // Lit -> - Val | Val
    protected void Lit()
    {
        if (currentToken.getId() == Token.Minus)
        {
            match(Token.Minus);
            Val();
        }
        else
        {
            Val();
        }
    }
    
    // Val -> identifier | number  | SQRT(Exp) | (Exp) 
    protected void Val()
    {
        if (currentToken.getId() == Token.Identifier)
        {
            match(Token.Identifier);
        }
        else if (currentToken.getId() == Token.Number)
        {
            match(Token.Number);
        }
        else if (currentToken.getId() == Token.Sqrt)
        {
            match(Token.Sqrt);
            match(Token.LeftParen);
            Exp();
            match(Token.RightParen);
        }
        else
        {
            match(Token.LeftParen);
            Exp();
            match(Token.RightParen);
        }
    }
    
    // Cnd -> Exp Rel Exp 
    protected void Cnd()
    {
        Exp();
        Rel();
        Exp();
    }
    
    // Rel -> < | = | > | <= | != | >=
    protected void Rel()
    {
        switch(currentToken.getId())
        {
            case Token.LessThan:
               match(Token.LessThan);
               break;
            case Token.Equal:
               match(Token.Equal);
               break;
            case Token.GreaterThan:
               match(Token.GreaterThan);
               break;
            case Token.GTEqual:
               match(Token.GTEqual);
               break;
            case Token.NotEqual:
               match(Token.NotEqual);
               break;
            case Token.LTEqual:
               match(Token.LTEqual);
               break;
            default:
               parseError("Missing relational operator");
        }

    }
}