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
 * A Component_nature.
 */
@Entity
@Table(name = "component_nature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Component_nature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "nature", length = 50)
    private String nature;

    @Size(max = 255)
    @Column(name = "caracteristics", length = 255)
    private String caracteristics;

    @OneToMany(mappedBy = "component_nature")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Component> components = new HashSet<>();

    @ManyToOne
    private Unit_used unit_used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature;
    }

    public Component_nature nature(String nature) {
        this.nature = nature;
        return this;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCaracteristics() {
        return caracteristics;
    }

    public Component_nature caracteristics(String caracteristics) {
        this.caracteristics = caracteristics;
        return this;
    }

    public void setCaracteristics(String caracteristics) {
        this.caracteristics = caracteristics;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public Component_nature components(Set<Component> components) {
        this.components = components;
        return this;
    }

    public Component_nature addComponent(Component component) {
        components.add(component);
        component.setComponent_nature(this);
        return this;
    }

    public Component_nature removeComponent(Component component) {
        components.remove(component);
        component.setComponent_nature(null);
        return this;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public Unit_used getUnit_used() {
        return unit_used;
    }

    public Component_nature unit_used(Unit_used unit_used) {
        this.unit_used = unit_used;
        return this;
    }

    public void setUnit_used(Unit_used unit_used) {
        this.unit_used = unit_used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Component_nature component_nature = (Component_nature) o;
        if(component_nature.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, component_nature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Component_nature{" +
            "id=" + id +
            ", nature='" + nature + "'" +
            ", caracteristics='" + caracteristics + "'" +
            '}';
    }
}
