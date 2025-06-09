package aor.proj3t.enums;

public enum Role {
        ADMIN(1, "Admin"), USER(2,"User"), MANAGER(3,"Manager");
        private int id;
        private String name;
        Role(int id, String name) {
                this.id = id;
                this.name = name;
        }
        public int getId() {
                return id;
        }
        public String getName() {
                return name;
        }

        public static Role getById(int id) {
                for (Role role : Role.values()) {
                        if (role.getId() == id) {
                                return role;
                        }
                }
                throw new IllegalArgumentException("No user role found for id: " + id);
        }

        public static Role getByName(String name) {
                for (Role role : Role.values()) {
                        if (role.getName().equals(name)) {
                                return role;
                        }
                }
                throw new IllegalArgumentException("No user role found for name: " + name);
        }
}
