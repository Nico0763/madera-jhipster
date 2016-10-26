package com.madera.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Module_component.
 */
@Entity
@Table(name = "module_component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module_component implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Max(value = 999)
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    private Module module;

    @ManyToOne
    private Component component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Module_component quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Module getModule() {
        return module;
    }

    public Module_component module(Module module) {
        this.module = module;
        return this;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Component getComponent() {
        return component;
    }

    public Module_component component(Component component) {
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
        Module_component module_component = (Module_component) o;
        if(module_component.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, module_component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Module_component{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
