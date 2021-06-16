package com.s2i.lms.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.s2i.lms.domain.ImageStore} entity.
 */
public class ImageStoreDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageStoreDTO() {
    }

    public ImageStoreDTO(Long id, String name, String base64) {
        this.id = id;
        this.name = name;
        this.base64 = base64;
    }

    private Long id;

    private String name;

    private String description;

    private String store;

    private String base64;

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

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
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
