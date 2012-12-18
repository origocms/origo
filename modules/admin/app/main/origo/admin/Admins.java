package main.origo.admin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

public class Admins {

    public static Set<String> pages = Sets.newHashSet();
    public static Map<String, String> aliases = Maps.newHashMap();

    public static void invalidate() {
        pages.clear();
        aliases.clear();
    }

    public static String getSafeString(String name) {
        if (name != null) {
            String trimmedValue = StringUtils.trim(name);
            return trimmedValue.replaceAll("/", "_");
        }
        return "";
    }

    public static String getAliasForPageName(String page) {
        for (String alias : aliases.keySet()) {
            String aliasPage = aliases.get(alias);
            if (aliasPage.equals(page)) {
                return alias;
            }
        }
        return null;
    }

}
