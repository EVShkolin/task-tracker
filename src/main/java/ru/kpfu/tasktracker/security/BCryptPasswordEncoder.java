package ru.kpfu.tasktracker.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder {

    private final int rounds;

    public BCryptPasswordEncoder() {
        this.rounds = 12;
    }

    public BCryptPasswordEncoder(int rounds) {
        this.rounds = rounds;
    }

    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(rounds));
    }

    public boolean matches(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }

}
