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
 * A Module.
 */
@Entity
@Table(name = "module")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Lob
    @Column(name = "cctp")
    private byte[] cctp;

    @Column(name = "cctp_content_type")
    private String cctpContentType;

    @Column(name = "price")
    private Float price;

    @ManyToOne
    private Principal_cross_section principal_cross_section;

    @ManyToOne
    private Module_nature module_nature;

    @OneToMany(mappedBy = "module")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    private Assortment assortment;

    @OneToMany(mappedBy = "module")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module_component> module_components = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Module name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getCctp() {
        return cctp;
    }

    public Module cctp(byte[] cctp) {
        this.cctp = cctp;
        return this;
    }

    public void setCctp(byte[] cctp) {
        this.cctp = cctp;
    }

    public String getCctpContentType() {
        return cctpContentType;
    }

    public Module cctpContentType(String cctpContentType) {
        this.cctpContentType = cctpContentType;
        return this;
    }

    public void setCctpContentType(String cctpContentType) {
        this.cctpContentType = cctpContentType;
    }

    public Float getPrice() {
        return price;
    }

    public Module price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Principal_cross_section getPrincipal_cross_section() {
        return principal_cross_section;
    }

    public Module principal_cross_section(Principal_cross_section principal_cross_section) {
        this.principal_cross_section = principal_cross_section;
        return this;
    }

    public void setPrincipal_cross_section(Principal_cross_section principal_cross_section) {
        this.principal_cross_section = principal_cross_section;
    }

    public Module_nature getModule_nature() {
        return module_nature;
    }

    public Module module_nature(Module_nature module_nature) {
        this.module_nature = module_nature;
        return this;
    }

    public void setModule_nature(Module_nature module_nature) {
        this.module_nature = module_nature;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Module products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Module addProduct(Product product) {
        products.add(product);
        product.setModule(this);
        return this;
    }

    public Module removeProduct(Product product) {
        products.remove(product);
        product.setModule(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Assortment getAssortment() {
        return assortment;
    }

    public Module assortment(Assortment assortment) {
        this.assortment = assortment;
        return this;
    }

    public void setAssortment(Assortment assortment) {
        this.assortment = assortment;
    }

    public Set<Module_component> getModule_components() {
        return module_components;
    }

    public Module module_components(Set<Module_component> module_components) {
        this.module_components = module_components;
        return this;
    }

    public Module addModule_component(Module_component module_component) {
        module_components.add(module_component);
        module_component.setModule(this);
        return this;
    }

    public Module removeModule_component(Module_component module_component) {
        module_components.remove(module_component);
        module_component.setModule(null);
        return this;
    }

    public void setModule_components(Set<Module_component> module_components) {
        this.module_components = module_components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Module module = (Module) o;
        if(module.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, module.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Module{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cctp='" + cctp + "'" +
            ", cctpContentType='" + cctpContentType + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
