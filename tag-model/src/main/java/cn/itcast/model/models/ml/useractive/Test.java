package cn.itcast.model.models.ml.useractive;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    static final String regex = "\\[(.*?)\\]";

    public static void main(String[] args) {
        double[] c1 = new double[]{0.6333333333333333, 0.1, 0.26666666666666666};
        double[] c2 = new double[]{0.6911375661375662, 0.10912698412698413, 0.1997354497354497};
        double[] c3 = new double[]{0.7307692307692307, 0.07692307692307693, 0.19230769230769232};

//		System.out.println(Arrays.stream(c1).mapToObj(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(3, BigDecimal.ROUND_DOWN).doubleValue());
//		System.out.println(Arrays.stream(c2).mapToObj(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(3, BigDecimal.ROUND_DOWN).doubleValue());
//		System.out.println(Arrays.stream(c3).mapToObj(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(3, BigDecimal.ROUND_DOWN).doubleValue());

        ArrayList<RFEBean> list = new ArrayList<RFEBean>() {{
            add(new RFEBean("1", "20190719", 1, 1));
            add(new RFEBean("1", "20190809", 1, 2));
            add(new RFEBean("1", "20190721", 1, 2));
            add(new RFEBean("1", "20190721", 1, 4));
            add(new RFEBean("2", "20190723", 1, 3));
            add(new RFEBean("2", "20190720", 1, 1));
            add(new RFEBean("2", "20190719", 1, 2));
            add(new RFEBean("3", "20190719", 1, 5));
            add(new RFEBean("3", "20190719", 1, 2));
            add(new RFEBean("3", "20190719", 1, 1));
            add(new RFEBean("4", "20190719", 1, 4));
            add(new RFEBean("4", "20190719", 1, 1));
        }};

        list.forEach(b -> {
            System.out.println(b.computeScore());
        });

        //System.out.println(getName("utime[GT]2019-01-01"));

    }

    private static String getName(String content) {
        Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\](.*+)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("==== " + matcher.group(0));
            System.out.println("==== " + matcher.group(1));
            System.out.println("==== " + matcher.group(2));
            System.out.println("==== " + matcher.group(3));
            return "";
        }
        return null;
    }

    private static String getExpression(String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String expression = matcher.group(1);
            return expression;
        }
        return null;
    }

}
