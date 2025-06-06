## Entity to DTO Conversion Example

When you call `userConverter.toDTO(userEntity)`, here's what happens:

```java
// Example using User entity -> UserDTO conversion

// 1. Start with a User entity fetched from database
User user = em.find(User.class, 123L);

// 2. Call the converter through the factory
UserDTO userDTO = converterFactory.forUser().toDTO(user);

// 3. Behind the scenes in AbstractConverter.toDTO():
public D toDTO(E entity) {
    if (entity == null) {
        return null;
    }

    // Create empty DTO instance
    D dto = createDTOInstance();  // Creates new UserDTO()

    // Map entity fields to DTO
    mapEntityToDTO(entity, dto);  // Calls UserConverter.mapEntityToDTO()
    return dto;
}

// 4. The UserConverter.mapEntityToDTO() implementation populates the DTO
protected void mapEntityToDTO(User user, UserDTO dto) {
    dto.setId(user.getId());                  // 123L
    dto.setEmail(user.getEmail());            // "john.doe@example.com"
    dto.setFirstName(user.getFirstName());    // "John"
    dto.setLastName(user.getLastName());      // "Doe"
    // Other fields...

    // Handle relationships
    if (user.getManager() != null) {
        dto.setManagerId(user.getManager().getId());  // 456L
    }
    // Other relationships...
}
```

## DTO to Entity Conversion Example

When you call `sessionConverter.toEntity(sessionDTO)`, here's what happens:

```java
// Example using SessionDTO -> Session entity conversion

// 1. Start with a SessionDTO (possibly from a REST endpoint)
SessionDTO sessionDTO = new SessionDTO();
sessionDTO.setId(789L);
sessionDTO.setSessionToken("abc123xyz");
sessionDTO.setUserId(123L);
sessionDTO.setActive(true);
sessionDTO.setCreatedAt(Instant.now());
sessionDTO.setExpirationDate(Instant.now().plus(30, ChronoUnit.MINUTES));

// 2. Call the converter through the factory
Session session = converterFactory.forSession().toEntity(sessionDTO);

// 3. Behind the scenes in AbstractConverter.toEntity():
public E toEntity(D dto) {
    if (dto == null) {
        return null;
    }

    E entity;
    ID id = getIdFromDTO(dto);  // Gets 789L from SessionDTO

    if (id != null) {
        // Try to find existing entity
        entity = em.find(getEntityClass(), id);  // Find Session with ID 789L
        if (entity == null) {
            entity = createEntityInstance();  // If not found, create new Session
        }
    } else {
        entity = createEntityInstance();  // Create new Session for a new record
    }

    mapDTOToEntity(dto, entity);  // Calls SessionConverter.mapDTOToEntity()
    return entity;
}

// 4. The SessionConverter.mapDTOToEntity() implementation updates the entity
protected void mapDTOToEntity(SessionDTO dto, Session session) {
    session.setSessionToken(dto.getSessionToken());  // "abc123xyz"
    session.setActive(dto.getActive());              // true
    session.setCreatedAt(dto.getCreatedAt());        // Current time
    session.setExpirationDate(dto.getExpirationDate()); // Current time + 30 min
    // Other fields...

    // Handle relationships using findSafely
    User user = findSafely(User.class, dto.getUserId());  // Find User with ID 123L
    session.setUser(user);  // Set the User relationship
}
```

## Key Points in the Process

1. **Smart Entity Lookup**: The `AbstractConverter.toEntity()` method tries to find existing entities to avoid duplicates.

2. **Null Safety**: All converters handle null values safely to prevent NullPointerExceptions.

3. **Relationship Management**: The `findSafely()` method provides clean relationship handling with null checks.

4. **Bidirectional Conversion**: The system supports both entity→DTO and DTO→entity conversions with specialized mapping.

5. **Separation of Concerns**: Each entity-DTO pair has its own converter that knows how to handle the specifics of that type.

The factory pattern with `ConverterFactory` provides a clean API for accessing the correct converter when needed in your services and resources.
