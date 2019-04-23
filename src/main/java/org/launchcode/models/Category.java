package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15, message = "Category name must be 3-15 characters.")
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Cheese> cheeses;

    public int getId() {
        return id;
    }

    public void setId() { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public List<Cheese> getCheeses() { return cheeses; }

    public void setCheeses(List<Cheese> cheeses) { this.cheeses = cheeses; }
}
