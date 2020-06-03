
public class ErrorHandler {
    public enum Error {
        //memory not enough
        E_MEM,
        // the expression is error
        E_BADEXPR,
        // [] not match
        E_PAREN,
        //to long
        E_LENGTH,
        //not start with [ 开头
        E_BRACKET,
        //^ not in the start of expression
        E_BOL,
        //* ? + the end of current expression
        E_CLOSE,
        E_NEWLINE,
    }

    private static String[] errMsgs = new String[]{
            "Not enough memory for NFA",
            "Malformed regular expression",
            "Missing close parenthesis",
            "Too many regular expression or expression too long",
            "Missing [ in character class",
            "^ must be at the start of expression or after [",
            "+ ? or * must follow an expression or subexpression",
            "Newline in quoted string, use \\n to get newline into expression",
            "Missing ) in macro expansion",
            "Macro deoesn't exist",
            "Macro expansions nested too deeply"
    };

    public static void parseErr(Error type) throws Exception {
//        System.out.println(errMsgs[type.ordinal()]);
        throw new Exception(errMsgs[type.ordinal()]);
    }
}
