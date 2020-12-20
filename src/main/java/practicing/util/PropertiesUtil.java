package practicing.util;

import cn.hutool.setting.dialect.Props;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
    private static Props props;

    private static Props getInstance(){
        if (null == props){
            props = new Props("common.properties");
            // 设置变量
            Set<Object> keySet = props.keySet();
            for (Object keyObj : keySet) {
                String key = keyObj.toString();
                String value = get(key);
                props.setProperty(key, value);
            }
        }
        return props;
    }

    public static String getStringValue(String key){
        return getInstance().getStr(key);
    }

    public static int getIntValue(String key){
        return getInstance().getInt(key);
    }

    public static long getLongValue(String key){
        return getInstance().getLong(key);
    }

    public static double getDoubleValue(String key){
        return getInstance().getDouble(key);
    }

    public static String get(String key) {
        String value = props.getStr(key);
        Matcher matcher = PATTERN.matcher(value);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String matcherKey = matcher.group(1);
            String matchervalue = props.getStr(matcherKey);
            if (matchervalue != null) {
                matcher.appendReplacement(buffer, matchervalue);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString().trim();
    }

    public static void main(String[] args) {
        System.out.println(PropertiesUtil.getStringValue("kafka.group.id"));
    }

}
