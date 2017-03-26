package ru.cdfe.gdr.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;
import ru.cdfe.gdr.constants.Relations;

import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Set;

@Document
@Data
@ToString(exclude = "secret")
@Relation(collectionRelation = Relations.USERS)
public class User implements Identifiable<String> {
    @Id
    @JsonIgnore
    private String id;
    
    @Version
    @JsonIgnore
    private BigInteger version;
    
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9]{1,128}")
    @Indexed(unique = true)
    private String name;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = ".{8,128}")
    private String secret;
    
    @NotEmpty
    private Set<Authority> authorities;
}
