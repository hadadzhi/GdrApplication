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
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.validation.groups.UserCreation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

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
    
    @JsonProperty(access = WRITE_ONLY)
    @Pattern(regexp = ".{8,256}")
    @NotNull(groups = UserCreation.class)
    private String secret;
    
    @NotEmpty
    private Set<Authority> authorities;
    
    @NotEmpty
    private Set<String> allowedAddresses;
}
