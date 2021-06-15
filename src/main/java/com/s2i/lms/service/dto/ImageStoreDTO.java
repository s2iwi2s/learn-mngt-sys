package com.s2i.lms.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.s2i.lms.domain.ImageStore} entity.
 */
public class ImageStoreDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageStoreDTO)) {
            return false;
        }

        ImageStoreDTO imageStoreDTO = (ImageStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageStoreDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", store='" + getStore() + "'" +
            "}";
    }
}
