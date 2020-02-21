package cn.itcast.model.model.tools.spark.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        getCondition("gender[[eq]1");

    }

    private static String[] getCondition(String whereField) {
        String[] condition = null;
        Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\](.*+)");
        Matcher matcher = pattern.matcher(whereField);

        if (matcher.find()) {
            System.out.println("==== " + matcher.group(0));
            System.out.println("==== " + matcher.group(1));
            System.out.println("==== " + matcher.group(2));
            System.out.println("==== " + matcher.group(3));
            if (matcher.groupCount() == 3) {
                condition = new String[3];
                condition[0] = matcher.group(1);
                condition[1] = matcher.group(2);
                condition[2] = matcher.group(3);
            }
        }
        return condition;
    }

}
