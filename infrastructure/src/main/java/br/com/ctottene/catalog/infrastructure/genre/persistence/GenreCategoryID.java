package br.com.ctottene.catalog.infrastructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "genre_id", nullable = false)
    private String genreID;

    @Column(name = "category_id", nullable = false)
    private String categoryID;

    public GenreCategoryID() {}

    private GenreCategoryID(final String aGenreID, final String aCategoryID) {
        this.genreID = aGenreID;
        this.categoryID = aCategoryID;
    }

    public static GenreCategoryID from(final String aGenreID, final String aCategoryID) {
        return new GenreCategoryID(aGenreID, aCategoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(getGenreID(), that.getGenreID()) && Objects.equals(getCategoryID(), that.getCategoryID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenreID(), getCategoryID());
    }

    public String getGenreID() {
        return genreID;
    }

    public void setGenreID(String genreID) {
        this.genreID = genreID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
