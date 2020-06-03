import java.sql.PreparedStatement;
import java.util.*;


public class Re {
    private Nfa start;

    private Lexer lexer;
    private NfaMachineConstructor constructor;
    private NfaMachineConstructor.NfaPair pair = new NfaMachineConstructor.NfaPair();

    Re(String regluar) throws Exception {
        lexer = new Lexer(regluar);
        start = new Nfa();
        try {
            constructor = new NfaMachineConstructor(lexer);
            constructor.complie(pair);
        } catch (Exception e) {
            throw e;
//            e.printStackTrace();
        }
        start = pair.startNode;
        if (pair.start == 1) {
            start.st = 1;
        }
    }


    private Set<Nfa> e_closure(Set<Nfa> input) {

        /*
         * Get the e closure corresponding to the nfa node in the input collection\
         * and add closure to the input
         */
//        System.out.print("ε-Closure( " + strFromNfaSet(input) + " ) = ");

        Stack<Nfa> nfaStack = new Stack<Nfa>();
        if (input.isEmpty()) {
            return null;
        }

        nfaStack.addAll(input);

        while (!nfaStack.empty()) {

            Nfa p = nfaStack.pop();

            if (p.next != null && p.getEdge() == Nfa.EPSILON) {

                if (!input.contains(p.next)) {
                    nfaStack.push(p.next);
                    input.add(p.next);
                }
            }

            if (p.next2 != null && p.getEdge() == Nfa.EPSILON) {

                if (!input.contains(p.next2)) {
                    nfaStack.push(p.next2);
                    input.add(p.next2);
                }
            }
        }
//        System.out.println("{ " + strFromNfaSet(input) + " }");
        return input;
    }

    private String strFromNfaSet(Set<Nfa> input) {
        StringBuilder s = new StringBuilder();
        Iterator it = input.iterator();
        while (it.hasNext()) {
            s.append(((Nfa) it.next()).getStateNum());
            if (it.hasNext()) {
                s.append(",");
            }
        }
        return s.toString();
    }

    private Set<Nfa> move(Set<Nfa> input, char c) {

        Set<Nfa> outSet = new HashSet<Nfa>();

        for (Nfa p : input) {
            Byte cb = (byte) c;
            if (p.getEdge() == c || (p.getEdge() == Nfa.CCL && p.inputSet.contains(cb))) {
                outSet.add(p.next);
            }
        }
        if (!outSet.isEmpty()) {
//            System.out.print("move({ " + strFromNfaSet(input) + " }, '" + c + "')= ");
//            System.out.println("{ " + strFromNfaSet(outSet) + " }");
        }
        return outSet;
    }

    String match(String str) {
        if (start.st == 1) {
            return matchAll(str);
        } else {
            return match0(str);
        }
    }

    String match0(String str) {
//        if (start.st == 1) {
//            matchAll(str);
//        }
        Set<Nfa> next = new HashSet<>();
        next.add(start);
        e_closure(next);
        Set<Nfa> current;

        StringBuilder inputStr = new StringBuilder();
        char ch;
        for (int i = 0; i < str.length(); i++) {
            current = move(next, (ch = str.charAt(i)));
            next = e_closure(current);
            if (next == null) {
                break;
            }
            inputStr.append(ch);
        }

//        System.out.println("The Nfa Machine can recognize string: " + inputStr);
        return inputStr.toString();
    }


    String matchAll(String str) {
        for (int i = 0; i < str.length(); i++) {
            String res = match0(str.substring(i, str.length()));
            if (!"".equals(res)) {
                return res;
            }
        }
        return "";
    }

    String matchAll0(String str) {
        StringBuilder match = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String res = match0(str.substring(i, str.length()));
            if (!"".equals(res)) {
                match.append(res);
            }
        }
        return match.toString();
    }

    String[] matchAll1(String str) {
        List<String> match = new ArrayList<String>();
        int k = 0;
        for (int i = 0; i < str.length(); i++) {
            String res = match0(str.substring(i, str.length()));
            if (!"".equals(res)) {
                match.add(res);
            }
        }
        return match.toArray(new String[match.size()]);
    }

    String sub(String from, String to) {
        return from.replaceAll(match(from), to);
    }

    void printNfa() {
        NfaPrinter printer = new NfaPrinter();
        printer.printNfa(start);
    }

    private boolean hasAcceptState(Set<Nfa> input) {
        boolean isAccepted = false;
        if (input == null || input.isEmpty()) {
            return false;
        }
        StringBuilder acceptedStatement = new StringBuilder("Accept State: ");
        for (Nfa p : input) {
            if (p.next == null && p.next2 == null) {
                isAccepted = true;
                acceptedStatement.append(p.getStateNum()).append(" ");
            }
        }
        if (isAccepted) {
            System.out.println(acceptedStatement.toString());
        }
        return isAccepted;
    }
}

