package models.origo.core;

import javax.persistence.TypedQuery;

public class QueryUtil {

    public static <T> void addPagination(TypedQuery<T> query, int page, int pageSize) {
        if (page > 1) {
            query.setFirstResult((page-1) * pageSize);
        }
        query.setMaxResults(page*pageSize);
    }

}
