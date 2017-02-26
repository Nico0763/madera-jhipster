package com.madera.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Component_product.
 */
@Entity
@Table(name = "component_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Component_product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Max(value = 360)
    @Column(name = "angle")
    private Integer angle;

    @Column(name = "length")
    private Float length;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Component component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAngle() {
        return angle;
    }

    public Component_product angle(Integer angle) {
        this.angle = angle;
        return this;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Float getLength() {
        return length;
    }

    public Component_product length(Float length) {
        this.length = length;
        return this;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Product getProduct() {
        return product;
    }

    public Component_product product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Component getComponent() {
        return component;
    }

    public Component_product component(Component component) {
        this.component = component;
        return this;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Component_product component_product = (Component_product) o;
        if(component_product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, component_product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Component_product{" +
            "id=" + id +
            ", angle='" + angle + "'" +
            ", length='" + length + "'" +
            '}';
    }
}
