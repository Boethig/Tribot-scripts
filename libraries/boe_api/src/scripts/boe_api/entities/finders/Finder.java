package scripts.boe_api.entities.finders;

import org.tribot.api.interfaces.Clickable07;
import org.tribot.api.types.generic.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Laniax
 */
public abstract class Finder<T extends Clickable07, S> extends FinderResult<T> implements Supplier<S> {

    protected final List<Filter<T>> filters;

    protected boolean or;

    protected Finder() {
        filters = new ArrayList<>();
        or = false;
    }

    protected Filter<T> buildFilter() {
        Filter<T> result = null;

        for (Filter<T> filter : filters) {

            if (result == null) {
                result = filter;
                continue;
            }

            result = or ? result.or(filter, false) : result.combine(filter, false);
        }

        return result;
    }

    /**
     * Apply a lambda as a custom filter. Example usage:
     * {@code .custom((obj) -> obj.isOnScreen()}
     * Which would only returns obj's that are on screen.
     * @param lambda <T> the lambda to execute
     * @return
     */
    @SuppressWarnings("unchecked")
    public S custom(Predicate<T> lambda) {

        filters.add(new Filter<T>() {
            @Override
            public boolean accept(T entity) {
                return lambda.test(entity);
            }
        });

        return (S)this;
    }

    public S or(boolean or) {
        this.or = or;
        return (S)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public S get() {
        return (S)this;
    }

}
