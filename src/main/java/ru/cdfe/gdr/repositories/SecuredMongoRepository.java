package ru.cdfe.gdr.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
interface SecuredMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    <S extends T> List<S> save(Iterable<S> entities);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    <S extends T> S insert(S entity);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    <S extends T> List<S> insert(Iterable<S> entities);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    <S extends T> S save(S entity);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    void delete(ID s);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    void delete(T entity);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    void delete(Iterable<? extends T> entities);
    
    @Override
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).ADMIN)")
    void deleteAll();
}
