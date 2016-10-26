package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @ManyToOne
    private Pattern pattern;

    @ManyToOne
    private Module module;

    @ManyToOne
    private Quotation quotation;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Component_product> component_products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Product pattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Module getModule() {
        return module;
    }

    public Product module(Module module) {
        this.module = module;
        return this;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public Product quotation(Quotation quotation) {
        this.quotation = quotation;
        return this;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public Set<Component_product> getComponent_products() {
        return component_products;
    }

    public Product component_products(Set<Component_product> component_products) {
        this.component_products = component_products;
        return this;
    }

    public Product addComponent_product(Component_product component_product) {
        component_products.add(component_product);
        component_product.setProduct(this);
        return this;
    }

    public Product removeComponent_product(Component_product component_product) {
        component_products.remove(component_product);
        component_product.setProduct(null);
        return this;
    }

    public void setComponent_products(Set<Component_product> component_products) {
        this.component_products = component_products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if(product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
