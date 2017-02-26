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
 * A Principal_cross_section.
 */
@Entity
@Table(name = "principal_cross_section")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Principal_cross_section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "principal_cross_section")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module> modules = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Principal_cross_section name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Principal_cross_section description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public Principal_cross_section url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Principal_cross_section modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Principal_cross_section addModule(Module module) {
        modules.add(module);
        module.setPrincipal_cross_section(this);
        return this;
    }

    public Principal_cross_section removeModule(Module module) {
        modules.remove(module);
        module.setPrincipal_cross_section(null);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Principal_cross_section principal_cross_section = (Principal_cross_section) o;
        if(principal_cross_section.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, principal_cross_section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Principal_cross_section{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
