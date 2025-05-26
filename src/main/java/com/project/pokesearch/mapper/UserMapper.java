package com.project.pokesearch.mapper;


import com.project.pokesearch.dto.UserDTO;
import com.project.pokesearch.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {FavoritePokemonMapper.class, TeamMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(source = "favoritePokemons", target = "favoritePokemons")
    @Mapping(source = "teams", target = "teams")
    UserDTO toDTO(User user);


    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "favoritePokemons", ignore = true) 
    @Mapping(target = "teams", ignore = true)
    User toEntity(UserDTO userDTO);
}
