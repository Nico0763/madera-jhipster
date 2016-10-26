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
 * A Module_nature.
 */
@Entity
@Table(name = "module_nature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module_nature implements Serializable {

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

    @OneToMany(mappedBy = "module_nature")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module> modules = new HashSet<>();

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

    public Module_nature nature(String nature) {
        this.nature = nature;
        return this;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCaracteristics() {
        return caracteristics;
    }

    public Module_nature caracteristics(String caracteristics) {
        this.caracteristics = caracteristics;
        return this;
    }

    public void setCaracteristics(String caracteristics) {
        this.caracteristics = caracteristics;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Module_nature modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Module_nature addModule(Module module) {
        modules.add(module);
        module.setModule_nature(this);
        return this;
    }

    public Module_nature removeModule(Module module) {
        modules.remove(module);
        module.setModule_nature(null);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Unit_used getUnit_used() {
        return unit_used;
    }

    public Module_nature unit_used(Unit_used unit_used) {
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
        Module_nature module_nature = (Module_nature) o;
        if(module_nature.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, module_nature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Module_nature{" +
            "id=" + id +
            ", nature='" + nature + "'" +
            ", caracteristics='" + caracteristics + "'" +
            '}';
    }
}
