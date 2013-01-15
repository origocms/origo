package main.origo.admin.helpers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.admin.annotations.Admin;
import main.origo.core.InitializationException;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.NavigationElement;
import org.apache.commons.lang3.StringUtils;
import play.i18n.Messages;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class NavigationHelper {

    private static Map<String, NavigationData> navigation;

    public static List<NavigationElement> getNavigation(Node node) throws NodeLoadException {
        if (navigation == null) {
            navigation = initNavigationData();
        }
        return createNavigationStructure(navigation, node);
    }

    private static List<NavigationElement> createNavigationStructure(Map<String, NavigationData> navigationDataMap, Node node) throws NodeLoadException {
        List<NavigationElement> result = Lists.newArrayList();
        for (NavigationData navigationData : navigationDataMap.values()) {
            result.addAll(createNavigationElements(navigationData, node));
        }
        Collections.sort(result);
        return result;
    }

    private static List<NavigationElement> createNavigationElements(NavigationData navigationData, Node node) throws NodeLoadException {
        List<NavigationElement> result = Lists.newArrayList();
        NavigationElement navigationElement = createNavigationElement(node, navigationData);
        for (String alias : navigationData.children.keySet()) {
            NavigationData nd = navigationData.children.get(alias);
            List<NavigationElement> navigationElements = createNavigationElements(nd, node);
            Collections.sort(navigationElements);
            navigationElement.children.addAll(navigationElements);
        }
        result.add(navigationElement);
        return result;
    }

    private static NavigationElement createNavigationElement(Node node, NavigationData navigationData) throws NodeLoadException {
        Map<String, Object> args = Maps.newHashMap();
        args.put("link", getLink(navigationData));
        args.put("key", navigationData.annotation.key());
        args.put("text", Messages.get(navigationData.annotation.key()));
        args.put("weight", navigationData.annotation.weight());
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NAVIGATION_ITEM, "origo.admin.navigation", args);
    }

    private static String getLink(NavigationData navigationData) throws NodeLoadException {
        try {
            return (String) navigationData.method.invoke(navigationData.method.getDeclaringClass());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NodeLoadException("-", "Unable to load navigation", e);
        }
    }

    private static Map<String, NavigationData> initNavigationData() {
        List<NavigationData> navigationDataList = readAllNavigationAnnotations();

        Map<String, NavigationData> result = Maps.newHashMap();
        for (NavigationData navigationData : navigationDataList) {
            String[] aliasSplit = StringUtils.split(navigationData.annotation.alias(), "/");
            addNavigationData(aliasSplit, result, navigationData);
        }
        return result;
    }

    private static void addNavigationData(String[] aliasSplit, Map<String, NavigationData> parent, NavigationData navigationData) {
        Map<String, NavigationData> current = parent;
        for (Iterator<String> iterator = Lists.newArrayList(aliasSplit).iterator(); iterator.hasNext();) {
            if (iterator.hasNext()) {
                String next = iterator.next();
                if (current.containsKey(next)) {
                    current = current.get(next).children;
                } else {
                    if (iterator.hasNext()) {
                        throw new InitializationException("Unable to add ["+navigationData.annotation.alias()+"] to navigation, missing parent ["+next+"].");
                    }
                    current.put(next, navigationData);
                }
            }
        }
    }

    private static List<NavigationData> readAllNavigationAnnotations() {

        List<CachedAnnotation> annotations = InterceptorRepository.getInterceptors(Admin.Navigation.class);
        List<NavigationData> navigationDataList = Lists.newArrayList();

        for(CachedAnnotation cachedAnnotation : annotations) {
            Admin.Navigation adminAnnotation = (Admin.Navigation) cachedAnnotation.annotation;
            assertAnnotationData(adminAnnotation);
            navigationDataList.add(new NavigationData(adminAnnotation, cachedAnnotation.method));
        }

        Collections.sort(navigationDataList, new Comparator<NavigationData>() {
            @Override
            public int compare(NavigationData n1, NavigationData n2) {
                return new Integer(n1.annotation.alias().length()).compareTo(n2.annotation.alias().length());
            }
        });
        return navigationDataList;
    }

    private static void assertAnnotationData(Admin.Navigation adminAnnotation) {
        String alias = adminAnnotation.alias();
        String key = adminAnnotation.key();
        if (StringUtils.isBlank(key)) {
            throw new InitializationException("Admin.Navigation key can't be empty");
        }
        if (StringUtils.isBlank(alias)) {
            throw new InitializationException("Admin.Navigation alias can't be empty");
        }
    }

    private static class NavigationData {
        public Admin.Navigation annotation;
        public Method method;

        public Map<String, NavigationData> children;

        private NavigationData(Admin.Navigation annotation, Method method) {
            this.annotation = annotation;
            this.method = method;
            this.children = Maps.newHashMap();
        }
    }

}
