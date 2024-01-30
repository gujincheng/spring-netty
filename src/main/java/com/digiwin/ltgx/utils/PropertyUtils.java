package com.digiwin.ltgx.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.UTF_8;
import static com.digiwin.ltgx.utils.PlaceholderUtils.PLACEHOLDER_PREFIX;
import static com.digiwin.ltgx.utils.PlaceholderUtils.PLACEHOLDER_SUFFIX;


/**
 * property utils
 * single instance
 */
public class PropertyUtils {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    private static final Properties properties = new Properties();

    private static final PropertyUtils propertyUtils = new PropertyUtils();

    public static final String DATASOURCE_PROPERTIES = "/datasource.properties";

    public static final String REGEX_COLON = ":";

    private PropertyUtils() {
        init();
    }

    /**
     * init
     */
    private void init() {
        loadConfigFromPropertiesFile(DATASOURCE_PROPERTIES, properties);
    }

    /**
     * get property value
     *
     * @param key property name
     * @return get string value
     */
    public static String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * get property value
     *
     * @param key        property name
     * @param defaultVal default value
     * @return property value
     */
    public static String getString(String key, String defaultVal) {
        String val = properties.getProperty(key.trim());
        return val == null ? defaultVal : val;
    }

    /**
     * get property value
     *
     * @param key property name
     * @return get property int value , if key == null, then return -1
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * get property value
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return get property int value，if key == null ，then return  defaultValue
     */
    public static int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.info(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * get property value
     *
     * @param key property name
     * @return property value
     */
    public static Boolean getBoolean(String key) {
        String value = properties.getProperty(key.trim());
        if (null != value) {
            return Boolean.parseBoolean(value);
        }

        return false;
    }

    /**
     * get property value
     *
     * @param key          property name
     * @param defaultValue default value
     * @return property value
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key.trim());
        if (null != value) {
            return Boolean.parseBoolean(value);
        }

        return defaultValue;
    }

    /**
     * get property long value
     *
     * @param key        key
     * @param defaultVal default value
     * @return property value
     */
    public static long getLong(String key, long defaultVal) {
        String val = getString(key);
        return val == null ? defaultVal : Long.parseLong(val);
    }

    /**
     * load config from properties file
     *
     * @param path
     * @param properties
     */
    public static void loadConfigFromPropertiesFile(String path, Properties properties) {
        String[] propertyFiles = new String[]{path};
        for (String fileName : propertyFiles) {
            InputStream fis = null;
            try {
                fis = PropertyUtils.class.getResourceAsStream(fileName);
                properties.load(convertFromEnv(fis));

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                if (fis != null) {
                    IOUtils.closeQuietly(fis);
                }
                System.exit(1);
            } finally {
                IOUtils.closeQuietly(fis);
            }
        }
    }

    /**
     * 替换 InputStream 中的环境变量
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static InputStream convertFromEnv(InputStream inputStream) throws IOException {
        String res = readFromInputStream(inputStream);
        String s = replacePlaceHolderWithEnv(res);
        byte[] bytes = s.getBytes();
        return new ByteArrayInputStream(bytes);
    }

    /**
     * InputStream 转换成 string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readFromInputStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream resultStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                resultStream.write(buffer, 0, length);
            }
            return resultStream.toString(UTF_8);
        }
    }

    /**
     * 使用环境变量替换目标字符串中的占位符，支持识别默认值
     *
     * @param strs
     * @return
     */
    public static String replacePlaceHolderWithEnv(String strs) {
        Map<String, String> env = System.getenv();
//        AtomicReference<String> str = new AtomicReference<>("Hello ${name:2}, welcome to ${TMP:1}  ${tmp:ZCQ}!");
        AtomicReference<String> str = new AtomicReference<>(strs);
        // 替换不包含默认值的环境变量，匹配不到则为源字符串
        str.set(PlaceholderUtils.replacePlaceholders(str.get(), env, true));

        // 获取带有默认值的变量字符串
        List<String> placeholdersWithColon = getPlaceholdersContainsColon(str.get());

        if (CollectionUtils.isNotEmpty(placeholdersWithColon)) {
            placeholdersWithColon.forEach(i -> {
                String defaultVal = "";
                String[] split = i.split(REGEX_COLON, 2); // 按照第一个冒号切分
                String key = split[0];
                if (split.length > 1) {
                    // 默认值不为空时
                    defaultVal = split[1];
                }
                String envValOrDefault = Optional.ofNullable(env.get(key)).orElse(defaultVal);
                str.set(str.get().replace(PLACEHOLDER_PREFIX + i + PLACEHOLDER_SUFFIX, envValOrDefault));
            });

        }
        return str.get();
    }

    /**
     * 匹配带有冒号的占位符 如: ${key:123}
     *
     * @param str
     * @return
     */
    public static List<String> getPlaceholdersContainsColon(String str) {
        List<String> placeholders = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\$\\{([^}]*:[^}]*)\\}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            placeholders.add(placeholder);
        }
        return placeholders;
    }
}
