import org.junit.Test;

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
}
