import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;

import java.util.Arrays;

public class TestNfa {

    /**
     * variant 6  \, ^, ., $, *, +, [ ], [^ ], { }.
     * [] [^] . * + , ^         \ {} $
     */
    public static void main(String[] args) {

    }

    /**
     * \\*
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_star_closure() throws Exception {
        Re re = new Re("3*");
        String ma1 = re.match("3");
        String ma2 = re.match("33");
        String ma3 = re.match("333333");
        assert "3".equals(ma1);
        assert "33".equals(ma2);
        assert "333333".equals(ma3);
    }

    /**
     * +
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_plus_closure() throws Exception {
        Re re = new Re("3+");
        String ma1 = re.match("33");
        String ma2 = re.match("333");
        System.out.println(ma2);
        assert "33".equals(ma1);
        assert "333".equals(ma2);
    }

    /***
     * \\.
     * @throws Exception
     */
    @org.junit.Test
    public void test_dot_closure() throws Exception {
        Re re = new Re("3.");
        String ma1 = re.match("32");
        String ma2 = re.match("3");
        assert "32".equals(ma1);
        assert "3".equals(ma2);
    }

    /**
     * []
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_CCL_closure() throws Exception {
        Re re = new Re("[2-9]");
        String ma1 = re.match("7");
        String ma2 = re.match("1");
        assert "7".equals(ma1);
        assert "".equals(ma2);

    }

    /**
     * [^]
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_ACCL_closure() throws Exception {
        Re re = new Re("[^1-8]");
        String ma1 = re.match("9");
        String ma2 = re.match("8");
        assert "9".equals(ma1);
        assert "".equals(ma2);
    }

    /**
     * ^
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_AT_B() throws Exception {
        Re re = new Re("^8");
        String match = re.match("8.rqew");
        assert "8".equals(match);
    }

    /**
     * $
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_AT_E() throws Exception {
        Re re = new Re("[1-9]*a$");
        String match = re.match("556a$");
        assert "556a".equals(match);
    }

    /**
     * test match method
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_match_method() throws Exception {
        Re re = new Re("[1-9]*qq.[a-z]*");
        String match = re.match("6545qq.com");
        assert "6545qq.com".equals(match);
    }

    /**
     * test sub method
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_sub_method() throws Exception {
        Re re = new Re("[1-9]*qq.[a-z]*");
        String sub = re.sub("6545qq.comabcdefg999", "11111");
        assert "11111999".equals(sub);
    }

    /**
     * \
     * get the graph
     *
     * @throws Exception
     */
    @org.junit.Test
    public void getGraph() throws Exception {
        Re re = new Re("[1-9]*qq.[a-z]*a+[^1-2]");
        re.match("6545qq.comabcdefg999");
    }

    /**
     * test email phone num;
     *
     * @throws Exception
     */
    @org.junit.Test
    public void test_email() throws Exception {
        Re re1 = new Re("[1-9]*@[a-z]*.com");
        String s = re1.matchAll("feijiqq653574281@qq.com12413241");
        assert "653574281@qq.com".equals(s);
    }

    @org.junit.Test
    public void test_part() throws Exception {
        Re re1 = new Re("[1-9]*@[a-z]*.com");
        String s = re1.matchAll("feijiqq653574281@qq.com12413241");
        assert "653574281@qq.com".equals(s);
    }

    /**
     * no match test
     */
    @Test
    public void test_no_match() throws Exception {
        Re re = new Re("[^5-9]*");
        String s = re.match("999");
        assert "".equals(s);
    }

    /**
     * Add negative test , wrong regular expression
     */
    @Test
    public void test_worry_expression() {
        try {
            Re re1 = new Re("]6");
        } catch (Exception e) {
            assert "Missing [ in character class".equals(e.getMessage());
        }
        try {
            Re re2 = new Re("*55");
        } catch (Exception e) {
            assert "+ ? or * must follow an expression or subexpression".equals(e.getMessage());
        }
        try {
            Re re3 = new Re("+[5-7]");
        } catch (Exception e) {
            assert "+ ? or * must follow an expression or subexpression".equals(e.getMessage());
        }
        try {
            Re re4 = new Re("?[5-7]");
        } catch (Exception e) {
            assert "+ ? or * must follow an expression or subexpression".equals(e.getMessage());
        }
        try {
            Re re5 = new Re("2^");
        } catch (Exception e) {
            assert "^ must be at the start of expression or after [".equals(e.getMessage());
        }
    }

    /**
     * test with begin and end lines
     */
    @Test
    public void testBeginAndEnd() throws Exception {
        Re re1 = new Re("(^[a-z]+)([0-9]+)([A-Z]+$)");
        Re re2 = new Re("([a-z]+)([0-9]+)([A-Z]+)");

        String match1 = re1.match("a1234A");
        String match2 = re2.match("a1234A");
        assert "a1234A".equals(match1);
        assert "a1234A".equals(match2);
        String match3 = re1.match("ABCa1234A");
        String match4 = re2.match("ABCa1234A");
        System.out.println(match3);
        assert "a1234A".equals(match3);
        assert "".equals(match4);


    }

    /**
     * add test with multiple matches
     */
    @Test
    public void testMultipleMatch() throws Exception {
        Re re = new Re("2");
        String match = re.match("2j2j2j2j2j");
        assert "2".equals(match);
        String s = re.matchAll0("2j2j2j2j2j");
        assert "22222".equals(s);
        String[] strings = re.matchAll1("2j2j2j2j2j");
        assert Arrays.equals(strings, new String[]{"2", "2", "2", "2", "2"});
    }

}
