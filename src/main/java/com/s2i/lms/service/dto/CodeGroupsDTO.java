package com.s2i.lms.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.s2i.lms.domain.CodeGroups} entity.
 */
public class CodeGroupsDTO implements Serializable {

    private Long id;

    private String code;

    private String value;

    private String description;

    private String json;

    private Integer priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeGroupsDTO)) {
            return false;
        }

        CodeGroupsDTO codeGroupsDTO = (CodeGroupsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codeGroupsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeGroupsDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", value='" + getValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", json='" + getJson() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
