package models;

public class Category {

    private String name;
    private int id;

    public Category(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;

        if (id != category.id) return false;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
