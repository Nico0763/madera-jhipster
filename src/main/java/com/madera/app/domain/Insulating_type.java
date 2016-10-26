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
 * A Insulating_type.
 */
@Entity
@Table(name = "insulating_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Insulating_type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @OneToMany(mappedBy = "insulating_type")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assortment> assortments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Insulating_type name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Assortment> getAssortments() {
        return assortments;
    }

    public Insulating_type assortments(Set<Assortment> assortments) {
        this.assortments = assortments;
        return this;
    }

    public Insulating_type addAssortment(Assortment assortment) {
        assortments.add(assortment);
        assortment.setInsulating_type(this);
        return this;
    }

    public Insulating_type removeAssortment(Assortment assortment) {
        assortments.remove(assortment);
        assortment.setInsulating_type(null);
        return this;
    }

    public void setAssortments(Set<Assortment> assortments) {
        this.assortments = assortments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Insulating_type insulating_type = (Insulating_type) o;
        if(insulating_type.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, insulating_type.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Insulating_type{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
