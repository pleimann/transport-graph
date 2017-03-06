package org.pleimann.graph.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Port {
    protected String id;
    protected String name;
    protected String country;

    @JsonUnwrapped
    protected Location location;
}
