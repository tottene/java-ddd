package br.com.ctottene.catalog.domain.category;

import br.com.ctottene.catalog.domain.Identifier;
import br.com.ctottene.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class CategoryID extends Identifier {

    private final String value;

    private CategoryID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }


    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
