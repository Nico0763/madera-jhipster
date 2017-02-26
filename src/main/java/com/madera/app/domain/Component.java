package com.madera.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Component.
 */
@Entity
@Table(name = "component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Component implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[A-Z]-[A-Z]$")
    @Column(name = "reference")
    private String reference;

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Component_product> component_products = new HashSet<>();

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module_component> module_components = new HashSet<>();

    @ManyToOne
    private Provider provider;

    @ManyToOne
    private Component_nature component_nature;

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Command_component> command_components = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Component url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReference() {
        return reference;
    }

    public Component reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Set<Component_product> getComponent_products() {
        return component_products;
    }

    public Component component_products(Set<Component_product> component_products) {
        this.component_products = component_products;
        return this;
    }

    public Component addComponent_product(Component_product component_product) {
        component_products.add(component_product);
        component_product.setComponent(this);
        return this;
    }

    public Component removeComponent_product(Component_product component_product) {
        component_products.remove(component_product);
        component_product.setComponent(null);
        return this;
    }

    public void setComponent_products(Set<Component_product> component_products) {
        this.component_products = component_products;
    }

    public Set<Module_component> getModule_components() {
        return module_components;
    }

    public Component module_components(Set<Module_component> module_components) {
        this.module_components = module_components;
        return this;
    }

    public Component addModule_component(Module_component module_component) {
        module_components.add(module_component);
        module_component.setComponent(this);
        return this;
    }

    public Component removeModule_component(Module_component module_component) {
        module_components.remove(module_component);
        module_component.setComponent(null);
        return this;
    }

    public void setModule_components(Set<Module_component> module_components) {
        this.module_components = module_components;
    }

    public Provider getProvider() {
        return provider;
    }

    public Component provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Component_nature getComponent_nature() {
        return component_nature;
    }

    public Component component_nature(Component_nature component_nature) {
        this.component_nature = component_nature;
        return this;
    }

    public void setComponent_nature(Component_nature component_nature) {
        this.component_nature = component_nature;
    }

    public Set<Command_component> getCommand_components() {
        return command_components;
    }

    public Component command_components(Set<Command_component> command_components) {
        this.command_components = command_components;
        return this;
    }

    public Component addCommand_component(Command_component command_component) {
        command_components.add(command_component);
        command_component.setComponent(this);
        return this;
    }

    public Component removeCommand_component(Command_component command_component) {
        command_components.remove(command_component);
        command_component.setComponent(null);
        return this;
    }

    public void setCommand_components(Set<Command_component> command_components) {
        this.command_components = command_components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Component component = (Component) o;
        if(component.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Component{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", reference='" + reference + "'" +
            '}';
    }
}
