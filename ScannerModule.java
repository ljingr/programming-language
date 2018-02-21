/**
* ScannerModule class uses the Token class and
* defines a constructor (with a filename argument)
* that initializes the module, getToken(), and getLineNumber()
*
* @author Jingran Li
* @version 1.0
*/
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileInputStream;

public class ScannerModule
{
  private int lineNumber = 1;
  private int index = 0;
  private String currentLine;
  private File textFile;
  private Scanner input;

  private Token TokenType()
  {
    if (isFileEnd()) {
      return createToken(Token.EndOfFile, "");
    }
    char ch = nextChar();
    while (ch == '\t' || ch == ' ')
    {
      ch = nextChar();
    }

    int beginIndex = index - 1;
    switch (ch) {
      case '(': return createToken(Token.LeftParen, "(");
      case ')': return createToken(Token.RightParen, ")");
      case '.':
        if (isDecimalDigit(peekChar()))
        {
          return scanNumberPostPeriod(beginIndex);
        }
        return createToken(Token.Error, "");
      case ';': return createToken(Token.Semicolon, ";");
      case ',': return createToken(Token.Comma, ",");
      case ':':
        if (isEqual(peekChar()))
        {
          return createToken(Token.Assign, ":=");
        }
        return createToken(Token.Colon, ":");
      case '<':
        if (isEqual(peekChar()))
        {
          return createToken(Token.LTEqual, "<=");
        }
        return createToken(Token.LessThan, "<");
      case '>':
        if (isEqual(peekChar()))
        {
          return createToken(Token.GTEqual, ">=");
        }
        return createToken(Token.GreaterThan, ">");
      case '=': return createToken(Token.Equal, "=");
      case '!':
        if (isEqual(peekChar()))
        {
          return createToken(Token.NotEqual, "!=");
        }
        return createToken(Token.Error, "");
      case '*':
        if (isStar(peekChar()))
        {
          return createToken(Token.Raise, "**");
        }
        return createToken(Token.Multiply, "*");
      case '/':
        if (isComment(peekChar()))
        {
          currentLine = input.nextLine();
          lineNumber ++;
          index = 0;
          return TokenType();
        }
        return createToken(Token.Divide, "/");
      case '+': return createToken(Token.Plus, "+");
      case '-': return createToken(Token.Minus, "-");
      case '0':
        return scanPostZero(beginIndex);
      case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
        return scanPostDigit(beginIndex);
      case '"': return scanString(beginIndex,ch);
      default:
        return scanIdentifierOrKeyword(beginIndex,ch);
    }
  }

  private Token createToken(int id, String lexeme)
  {
    if (id == 0 || id == 4 || id == 14 || id == 18 || id == 19 || id == 20)
    {
      index ++;
    }
    return new Token(id, lexeme);
  }

  private char nextChar()
  {
    if (isLineEnd(0))
    {
      return '\0';
    }
    return currentLine.charAt(index++);
  }

  private boolean peek(char ch)
  {
    return peekChar() == ch;
  }

  private char peekChar()
  {
    return peekChar(0);
  }

  private char peekChar(int offset)
  {
    return isLineEnd(offset) ? '\0' : currentLine.charAt(index + offset);
  }


  private boolean isLineEnd(int x)
  {
    return (index + x >= currentLine.length());
  }

  private boolean isFileEnd()
  {
    return index >= currentLine.length() && !(input.hasNextLine());
  }

  private static boolean isEqual(char ch)
  {
    switch (ch)
    {
      case '=': return true;
      default: return false;
    }
  }

  private static boolean isComment(char ch)
  {
    switch (ch)
    {
      case '/': return true;
      default: return false;
    }
  }

  private static boolean isStar(char ch)
  {
    switch (ch)
    {
      case '*': return true;
      default: return false;
    }
  }

  private static boolean isDecimalDigit(char ch)
  {
    switch (ch)
    {
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        return true;
      default:
        return false;
    }
  }

  private Token scanNumberPostPeriod(int beginIndex)
  {
    skipDecimalDigits();
    return scanExponentOfNumeric(beginIndex);
  }

  private void skipDecimalDigits()
  {
    while (isDecimalDigit(peekChar()))
    {
      nextChar();
    }
  }

  private Token scanExponentOfNumeric(int beginIndex)
  {
    switch (peekChar())
    {
      case 'e':
      case 'E':
        nextChar();
        switch (peekChar()) {
          case '+':
          case '-':
            nextChar();
            break;
          default:
        }
        if (!isDecimalDigit(peekChar())) {
          return createToken(Token.Error, "");
        }
        skipDecimalDigits();
        break;
      default:
    }
    return createToken(Token.Number, currentLine.substring(beginIndex,index));
  }

  private Token scanPostDigit(int beginIndex)
  {
    skipDecimalDigits();
    return scanFractionalNumeric(beginIndex);
  }

  private Token scanFractionalNumeric(int beginIndex)
  {
    if (peek('.'))
    {
      nextChar();
      skipDecimalDigits();
    }
    return scanExponentOfNumeric(beginIndex);
  }

  private Token scanPostZero(int beginIndex)
  {
    switch (peekChar())
    {
      case 'e':
      case 'E':
        return scanExponentOfNumeric(beginIndex);
      case '.':
        return scanFractionalNumeric(beginIndex);
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        skipDecimalDigits();
        if (peek('.'))
        {
          nextChar();
          skipDecimalDigits();
        }
        return createToken(Token.Number, currentLine.substring(beginIndex,index));
      default:
        return createToken(Token.Number, currentLine.substring(beginIndex,index));
    }
  }

  private Token scanIdentifierOrKeyword(int beginIndex,char ch)
  {
    StringBuilder valueBuilder = new StringBuilder();
    valueBuilder.append(ch);

    ch = peekChar();
    while (isIdentifierPart(ch))
    {
      valueBuilder.append(nextChar());
      ch = peekChar();
    }

    String value = valueBuilder.toString();

    char start = value.charAt(0);
    if (!isIdentifierStart(start))
    {
      return createToken(Token.Error, "");
    }

    switch (value)
    {
      case "PRINT": return createToken(Token.Print, "PRINT");
      case "IF": return createToken(Token.If, "IF");
      case "ELSE": return createToken(Token.Else, "ELSE");
      case "ENDIF": return createToken(Token.Endif, "ENDIF");
      case "SQRT": return createToken(Token.Sqrt, "SQRT");
      case "AND": return createToken(Token.And, "AND");
      case "OR": return createToken(Token.Or, "OR");
      case "NOT": return createToken(Token.Not, "NOT");
      default: break;
    }
    return createToken(Token.Identifier, currentLine.substring(beginIndex,index));
  }

  private static boolean isIdentifierStart(char ch)
  {
    switch (ch)
    {
      case '_': return true;
      default: return Character.isLetter(ch);
    }
  }

  private static boolean isIdentifierPart(char ch)
  {
    return isIdentifierStart(ch) || Character.isDigit(ch);
  }

  private Token scanString (int beginIndex, char terminator)
  {
    while (!isLineEnd(0) && peekChar() != terminator)
    {
      nextChar();
    }
    if (peekChar() != terminator)
    {
      return createToken(Token.Error, "");
    } else {
      nextChar();
    }
    return createToken(Token.String, currentLine.substring(beginIndex,index));
  }

  private void readInFile(File textFile)
  {
    try
    {
      input = new Scanner(textFile);
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public ScannerModule(String fileName)
  {
    textFile = new File(fileName);
    readInFile(textFile);
    this.currentLine = input.nextLine();
    this.lineNumber = lineNumber;
    this.index = index;
  }

  public Token getToken()
  {

    if (index >= currentLine.length() && input.hasNextLine())
    {
      currentLine = input.nextLine();
      lineNumber ++;
      index = 0;
    }

    while (currentLine.length() == 0 )
    {
      currentLine = input.nextLine();
      lineNumber ++;
      index = 0;
    }

    while ((index+1 < currentLine.length()) && input.hasNextLine() && currentLine.charAt(index+0) == '/' && currentLine.charAt(index+1) == '/')
    {
      currentLine = input.nextLine();
      lineNumber ++;
      index = 0;
    }

    if (index+1 < currentLine.length() && !input.hasNextLine() && currentLine.charAt(index+0) == '/' && currentLine.charAt(index+1) == '/')
    {
      index = currentLine.length();
    }

    return TokenType();

  }


  public int getLineNumber()
  {
    return lineNumber;
  }
}
