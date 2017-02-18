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
 * A Pattern.
 */
@Entity
@Table(name = "pattern")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    private Assortment assortment;

    @OneToMany(mappedBy = "pattern")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Pattern name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public Pattern image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Pattern imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Assortment getAssortment() {
        return assortment;
    }

    public Pattern assortment(Assortment assortment) {
        this.assortment = assortment;
        return this;
    }

    public void setAssortment(Assortment assortment) {
        this.assortment = assortment;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Pattern products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Pattern addProduct(Product product) {
        products.add(product);
        product.setPattern(this);
        return this;
    }

    public Pattern removeProduct(Product product) {
        products.remove(product);
        product.setPattern(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pattern pattern = (Pattern) o;
        if(pattern.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pattern.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pattern{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            '}';
    }
}
