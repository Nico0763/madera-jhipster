package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Quotation.
 */
@Entity
@Table(name = "quotation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Max(value = 9)
    @Column(name = "state")
    private Integer state;

    @Column(name = "commercial_percentage")
    private Float commercial_percentage;
 
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[A-Z]{2}-[A-Z]{2}")
    @Column(name = "reference")
    private String reference;

    @ManyToOne
    private Assortment assortment;

    @OneToMany(mappedBy = "quotation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deadline> deadlines = new HashSet<>();

    @OneToMany(mappedBy = "quotation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Quotation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Quotation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getState() {
        return state;
    }

    public Quotation state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Float getCommercial_percentage() {
        return commercial_percentage;
    }

    public Quotation commercial_percentage(Float commercial_percentage) {
        this.commercial_percentage = commercial_percentage;
        return this;
    }

    public void setCommercial_percentage(Float commercial_percentage) {
        this.commercial_percentage = commercial_percentage;
    }

    public String getReference() {
        return reference;
    }

    public Quotation reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Assortment getAssortment() {
        return assortment;
    }

    public Quotation assortment(Assortment assortment) {
        this.assortment = assortment;
        return this;
    }

    public void setAssortment(Assortment assortment) {
        this.assortment = assortment;
    }

    public Set<Deadline> getDeadlines() {
        return deadlines;
    }

    public Quotation deadlines(Set<Deadline> deadlines) {
        this.deadlines = deadlines;
        return this;
    }

    public Quotation addDeadline(Deadline deadline) {
        deadlines.add(deadline);
        deadline.setQuotation(this);
        return this;
    }

    public Quotation removeDeadline(Deadline deadline) {
        deadlines.remove(deadline);
        deadline.setQuotation(null);
        return this;
    }

    public void setDeadlines(Set<Deadline> deadlines) {
        this.deadlines = deadlines;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Quotation products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Quotation addProduct(Product product) {
        products.add(product);
        product.setQuotation(this);
        return this;
    }

    public Quotation removeProduct(Product product) {
        products.remove(product);
        product.setQuotation(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Quotation customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quotation quotation = (Quotation) o;
        if(quotation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, quotation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Quotation{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", date='" + date + "'" +
            ", state='" + state + "'" +
            ", commercial_percentage='" + commercial_percentage + "'" +
            ", reference='" + reference + "'" +
            '}';
    }
}
