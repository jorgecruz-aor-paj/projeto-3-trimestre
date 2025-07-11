package aor.proj3t.enums;

import aor.proj3t.exceptions.NotFoundException;

/**
 * Enum representing the different user roles in the system.
 * Each role has an associated ID and name, and provides different
 * levels of access and permissions within the application.
 * 
 * @author Project Team
 * @version 1.0
 * @since 1.0
 */
public enum Role {
        /** Administrator role with full system access */
        ADMIN(1, "Admin"),

        /** Regular user role with standard permissions */
        USER(2, "User"),

        /** Manager role with elevated permissions */
        MANAGER(3, "Manager");

        /** The unique identifier for this role */
        private final int id;

        /** The display name for this role */
        private final String name;

        /**
         * Constructor for Role enum values.
         * 
         * @param id   The unique identifier for the role
         * @param name The display name for the role
         */
        Role(int id, String name) {
                this.id = id;
                this.name = name;
        }

        /**
         * Gets the unique identifier for this role.
         * 
         * @return The role ID
         */
        public int getId() {
                return id;
        }

        /**
         * Gets the display name for this role.
         * 
         * @return The role name
         */
        public String getName() {
                return name;
        }

        /**
         * Finds a Role by its unique identifier.
         * 
         * @param id The role ID to search for
         * @return The Role with the matching ID
         * @throws NotFoundException if no role is found for the given ID
         */
        public static Role getById(int id) {
                for (Role role : Role.values()) {
                        if (role.getId() == id) {
                                return role;
                        }
                }
                throw new NotFoundException(
                                "Invalid userRole",
                                "No user role found for id: " + id);
        }

        /**
         * Finds a Role by its display name.
         * 
         * @param name The role name to search for
         * @return The Role with the matching name
         * @throws NotFoundException if no role is found for the given name
         */
        public static Role getByName(String name) {
                for (Role role : Role.values()) {
                        if (role.getName().equals(name)) {
                                return role;
                        }
                }
                throw new NotFoundException(
                                "Invalid userRole",
                                "No user role found for name: " + name);
        }
}
