# BaseConverterTest - How It Works

## Overview
The `BaseConverterTest` is an abstract test class that provides a reusable testing framework for all converter utilities in the project. It implements common test patterns that can be inherited by specific converter test classes, ensuring consistent and comprehensive testing across all converters.

## Purpose
- **Standardization**: Provides a consistent testing approach for all converters
- **Code Reuse**: Eliminates duplicate test code across converter test classes
- **Completeness**: Ensures all converters are tested with the same set of basic operations
- **Maintainability**: Changes to testing patterns only need to be made in one place

## How It Works

### Generic Type Parameters
```java
public abstract class BaseConverterTest<E, D, C extends Converter<E, D>>
```
- `E`: The entity type (e.g., User, Training, etc.)
- `D`: The DTO type (e.g., UserDTO, TrainingDTO, etc.)
- `C`: The converter type (e.g., UserConverter, TrainingConverter, etc.)

### Abstract Methods (Must be implemented by subclasses)
1. **`getConverter()`**: Returns the converter instance being tested
2. **`createValidEntity()`**: Creates a valid entity instance for testing
3. **`createValidDTO()`**: Creates a valid DTO instance for testing
4. **`assertEntityEqualsDTO(E entity, D dto)`**: Verifies entity-to-DTO conversion
5. **`assertDTOEqualsEntity(D dto, E entity)`**: Verifies DTO-to-entity conversion

### Common Test Cases Provided
1. **Entity to DTO conversion** (`testEntityToDTO()`)
2. **DTO to Entity conversion** (`testDTOToEntity()`)
3. **Null handling** (various null scenarios)
4. **Collection conversions** (lists of entities/DTOs)

## Implementation Guidelines for Subclasses

### 1. Test Class Structure
```java
@ExtendWith(MockitoExtension.class)
@DisplayName("YourConverter Tests")
public class YourConverterTest extends BaseConverterTest<YourEntity, YourDTO, YourConverter> {
    
    @Mock
    private EntityManager entityManager;
    
    @InjectMocks
    private YourConverter yourConverter;
    
    @BeforeEach
    void setUp() {
        // Setup mocks for entity relationships
        lenient().when(entityManager.find(eq(SomeEntity.class), any(Long.class))).thenReturn(null);
    }
    
    // Implement required abstract methods...
}
```

### 2. Creating Test Data
When implementing `createValidEntity()` and `createValidDTO()`, follow these guidelines:

#### For Base Test Methods
- **Don't include relationships**: Base tests focus on simple field mapping
- **Use simple values**: Avoid complex nested objects
- **Be consistent**: Use the same data structure in both entity and DTO

Example:
```java
@Override
protected User createValidEntity() {
    User user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setFirstName("John");
    user.setLastName("Doe");
    // Don't set manager, workplace, etc. for base tests
    return user;
}
```

#### For Relationship-Specific Tests
- Create separate test methods for testing relationships
- Mock the EntityManager appropriately
- Test both null and valid relationship scenarios

### 3. Assertion Methods
When implementing assertion methods, be careful about what to check:

#### Entity-to-DTO Assertions (`assertEntityEqualsDTO`)
- Check all simple fields
- Include relationship IDs if the entity has them loaded
- Don't assume relationships are always present

#### DTO-to-Entity Assertions (`assertDTOEqualsEntity`)
- **Don't check entity ID**: IDs come from the database, not DTOs
- Check all simple fields that were mapped
- Don't check relationships in base assertions (test separately)

Example:
```java
@Override
protected void assertDTOEqualsEntity(UserDTO dto, User entity) {
    // DON'T check ID - it comes from database
    assertEquals(dto.getEmail(), entity.getEmail());
    assertEquals(dto.getFirstName(), entity.getFirstName());
    assertEquals(dto.getLastName(), entity.getLastName());
    // Don't check relationships here - test them separately
}
```

## Key Design Decisions

### Why IDs Are Not Checked in DTO-to-Entity Assertions
The `AbstractConverter` follows JPA conventions where:
- Entity IDs are managed by the persistence layer
- When converting DTO to entity, the converter either:
  - Finds an existing entity by ID (ID already set)
  - Creates a new entity (ID will be set when persisted)
- The DTO's ID is used for lookup, not for setting the entity's ID

### Why Relationships Are Tested Separately
- Base tests focus on simple field mapping
- Relationship testing requires complex mocking
- Separation allows for cleaner, more focused tests
- Different converters have different relationship patterns

## Common Patterns in Converter Tests

### 1. Mock Setup
```java
@BeforeEach
void setUp() {
    // Use lenient() to avoid strict stubbing issues
    lenient().when(entityManager.find(eq(SomeEntity.class), any(Long.class))).thenReturn(null);
}
```

### 2. Relationship Testing
```java
@Test
@DisplayName("Should convert entity with relationships to DTO")
void testEntityWithRelationshipsToDTO() {
    // Given
    RelatedEntity related = new RelatedEntity();
    related.setId(2L);
    
    YourEntity entity = createValidEntity();
    entity.setRelated(related);
    
    // When
    YourDTO result = converter.toDTO(entity);
    
    // Then
    assertNotNull(result);
    assertEquals(related.getId(), result.getRelatedId());
}
```

### 3. Null Handling
```java
@Test
@DisplayName("Should handle null relationships gracefully")
void testNullRelationships() {
    // Given
    YourEntity entity = createValidEntity();
    entity.setRelated(null);
    
    // When
    YourDTO result = converter.toDTO(entity);
    
    // Then
    assertNotNull(result);
    assertNull(result.getRelatedId());
}
```

## Benefits of This Approach

1. **Consistency**: All converters tested with same patterns
2. **Coverage**: Ensures basic functionality is always tested
3. **Maintainability**: Changes to testing patterns affect all converters
4. **Documentation**: Test structure itself documents expected behavior
5. **Reliability**: Reduces chance of missing important test cases

## Files Using This Pattern

All converter test classes extend `BaseConverterTest`:
- `UserConverterTest`
- `TrainingConverterTest`
- `EvaluationConverterTest`
- `WorkplaceConverterTest`
- `SessionConverterTest`
- `MessageConverterTest`
- `NotificationConverterTest`
- `ResetPasswordConverterTest`
- `SettingsConverterTest`
- `EvaluationCycleConverterTest`
- `PendingRegistrationConverterTest`
- `TrainingAreaConverterTest`
- `MiniUserConverterTest`
- `MiniEvaluationCycleConverterTest`
- `TrainingParticipationConverterTest`
- `ConverterFactoryTest`
