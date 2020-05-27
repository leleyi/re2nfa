
/**
 * @author author
 */
public class Lexer {
    /**
     * variant 6  \, ^, ., $, *, +, [ ], [^ ], { }.
     */
    public enum Token {
        // the end of regular expression
        EOS,
        // . any
        ANY,

        //^ start
        AT_BOL,

        //$ end
        AT_EOL,

        //]
        CCL_END,
        // -
        DASH,
        //[
        CCL_START,

        // {
        OPEN_CURLY,
        // }
        CLOSE_CURLY,

        //(
        OPEN_PAREN,
        //)
        CLOSE_PAREN,

        // * 0 or +
        CLOSURE,
        // + 1 or 1+
        PLUS_CLOSE,
        // ? 0 or 1
        OPTIONAL,

        // cll
        L,
        // |
        OR,
        //,
        COMMA
        //       END_OF_INPUT,
    }

    private final int ASCII_COUNT = 128;
    private Token[] tokenMap = new Token[ASCII_COUNT];
    private Token currentToken = Token.EOS;

    private int exprCount = 0;
    private String curExpr = "";
    private int charIndex = 0;

    public int getCharIndex() {
        return charIndex;
    }

    private boolean sawEsc = false;

    private int lexeme;


    public Lexer(String reString) {
        initTokenMap();
        this.curExpr = reString;
    }

    private void initTokenMap() {
        for (int i = 0; i < ASCII_COUNT; i++) {
            tokenMap[i] = Token.L;
        }
        tokenMap['.'] = Token.ANY;
        tokenMap[','] = Token.COMMA;
        tokenMap['^'] = Token.AT_BOL;
        tokenMap['$'] = Token.AT_EOL;
        tokenMap[']'] = Token.CCL_END;
        tokenMap['['] = Token.CCL_START;
        tokenMap['}'] = Token.CLOSE_CURLY;
        tokenMap[')'] = Token.CLOSE_PAREN;
        tokenMap['*'] = Token.CLOSURE;
        tokenMap['-'] = Token.DASH;
        tokenMap['{'] = Token.OPEN_CURLY;
        tokenMap['('] = Token.OPEN_PAREN;
        tokenMap['?'] = Token.OPTIONAL;
        tokenMap['|'] = Token.OR;
        tokenMap['+'] = Token.PLUS_CLOSE;
    }

    public boolean matchToken(Token t) {
        return currentToken == t;
    }

    public int getLexeme() {
        return lexeme;
    }

    public String getCurExpr() {
        return curExpr;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token advance() {

        if (charIndex >= curExpr.length()) {
            currentToken = Token.EOS;
            charIndex = 0;
            return currentToken;
        }

        sawEsc = (curExpr.charAt(charIndex) == '\\');

        if (sawEsc && curExpr.charAt(charIndex + 1) != '"') {
            lexeme = handleEsc();
        } else {
            if (sawEsc && curExpr.charAt(charIndex + 1) == '"') {
                charIndex += 2;
                lexeme = '"';
            } else {
                lexeme = curExpr.charAt(charIndex);
                charIndex++;
            }
        }
        currentToken = sawEsc ? Token.L : tokenMap[lexeme];
        return currentToken;
    }

    private int handleEsc() {
        /*当转移符 \ 存在时，它必须与跟在它后面的字符或字符串一起解读
         *我们处理的转义字符有以下几种形式
         * \b backspace
         * \f formfeed
         * \n newline
         * \r carriage return 回车
         * \s space 空格
         * \t tab
         * \e ASCII ESC ('\033')
         * \DDD 3位八进制数
         * \xDDD 3位十六进制数
         * \^C C是任何字符， 例如^A, ^B 在Ascii 表中都有对应的特殊含义
         * ASCII 字符表参见：
         * http://baike.baidu.com/pic/%E7%BE%8E%E5%9B%BD%E4%BF%A1%E6%81%AF%E4%BA%A4%E6%8D%A2%E6%A0%87%E5%87%86%E4%BB%A3%E7%A0%81/8950990/0/9213b07eca8065387d4c671896dda144ad348213?fr=lemma&ct=single#aid=0&pic=9213b07eca8065387d4c671896dda144ad348213
         */

        int rval = 0;
        String exprToUpper = curExpr.toUpperCase();
        charIndex++; //越过转移符 \
        switch (exprToUpper.charAt(charIndex)) {
            case '\0':
                rval = '\\';
                break;
            case 'B':
                rval = '\b';
                break;
            case 'F':
                rval = '\f';
                break;
            case 'N':
                rval = '\n';
                break;
            case 'R':
                rval = '\r';
                break;
            case 'S':
                rval = ' ';
                break;
            case 'T':
                rval = '\t';
                break;
            case 'E':
                rval = '\033';
                break;
            case '^':
                charIndex++;
                /*
                 * 因此当遇到^后面跟在一个字母时，表示读入的是控制字符
                 * ^@ 在ASCII 表中的数值为0，^A 为1, 字符@在ASCII 表中数值为80， 字符A在ASCII表中数值为81
                 * 'A' - '@' 等于1 就对应 ^A 在 ASCII 表中的位置
                 * 具体可参看注释给出的ASCII 图
                 *
                 */
                rval = (char) (curExpr.charAt(charIndex) - '@');
                break;
//            case 'X':
//                /*
//                 * \X 表示后面跟着的三个字符表示八进制或十六进制数
//                 */
//                charIndex++; //越过X
//                if (isHexDigit(curExpr.charAt(charIndex))) {
//                    rval = hex2Bin(curExpr.charAt(charIndex));
//                    charIndex++;
//                }
//
//                if (isHexDigit(curExpr.charAt(charIndex))) {
//                    rval <<= 4;
//                    rval |= hex2Bin(curExpr.charAt(charIndex));
//                    charIndex++;
//                }
//
//                if (isHexDigit(curExpr.charAt(charIndex))) {
//                    rval <<= 4;
//                    rval |= hex2Bin(curExpr.charAt(charIndex));
//                    charIndex++;
//                }
//                charIndex--; //由于在函数底部会对charIndex++ 所以这里先 --
//                break;
//
//            default:
//                if (isOctDigit(curExpr.charAt(charIndex)) == false) {
//                    rval = curExpr.charAt(charIndex);
//                } else {
//                    charIndex++;
//                    rval = oct2Bin(curExpr.charAt(charIndex));
//                    charIndex++;
//                    if (isOctDigit(curExpr.charAt(charIndex))) {
//                        rval <<= 3;
//                        rval |= oct2Bin(curExpr.charAt(charIndex));
//                        charIndex++;
//                    }
//
//                    if (isOctDigit(curExpr.charAt(charIndex))) {
//                        rval <<= 3;
//                        rval |= oct2Bin(curExpr.charAt(charIndex));
//                        charIndex++;
//                    }
//
//                    charIndex--;//由于在函数底部会对charIndex++ 所以这里先 --
//                }
        }

        charIndex++;
        return rval;
    }
}
