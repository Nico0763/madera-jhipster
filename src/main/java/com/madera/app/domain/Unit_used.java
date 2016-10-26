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
 * A Unit_used.
 */
@Entity
@Table(name = "unit_used")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Unit_used implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "regular_expression", length = 255)
    private String regular_expression;

    @OneToMany(mappedBy = "unit_used")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module_nature> module_natures = new HashSet<>();

    @OneToMany(mappedBy = "unit_used")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Component_nature> component_natures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Unit_used name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegular_expression() {
        return regular_expression;
    }

    public Unit_used regular_expression(String regular_expression) {
        this.regular_expression = regular_expression;
        return this;
    }

    public void setRegular_expression(String regular_expression) {
        this.regular_expression = regular_expression;
    }

    public Set<Module_nature> getModule_natures() {
        return module_natures;
    }

    public Unit_used module_natures(Set<Module_nature> module_natures) {
        this.module_natures = module_natures;
        return this;
    }

    public Unit_used addModule_nature(Module_nature module_nature) {
        module_natures.add(module_nature);
        module_nature.setUnit_used(this);
        return this;
    }

    public Unit_used removeModule_nature(Module_nature module_nature) {
        module_natures.remove(module_nature);
        module_nature.setUnit_used(null);
        return this;
    }

    public void setModule_natures(Set<Module_nature> module_natures) {
        this.module_natures = module_natures;
    }

    public Set<Component_nature> getComponent_natures() {
        return component_natures;
    }

    public Unit_used component_natures(Set<Component_nature> component_natures) {
        this.component_natures = component_natures;
        return this;
    }

    public Unit_used addComponent_nature(Component_nature component_nature) {
        component_natures.add(component_nature);
        component_nature.setUnit_used(this);
        return this;
    }

    public Unit_used removeComponent_nature(Component_nature component_nature) {
        component_natures.remove(component_nature);
        component_nature.setUnit_used(null);
        return this;
    }

    public void setComponent_natures(Set<Component_nature> component_natures) {
        this.component_natures = component_natures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unit_used unit_used = (Unit_used) o;
        if(unit_used.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, unit_used.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Unit_used{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", regular_expression='" + regular_expression + "'" +
            '}';
    }
}
