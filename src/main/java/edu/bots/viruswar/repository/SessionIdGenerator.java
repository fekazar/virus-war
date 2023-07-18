package edu.bots.viruswar.repository;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class SessionIdGenerator implements IdentifierGenerator {
    private final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        var sb = new StringBuilder();
        for (int i = 0; i < 10; ++i)
            sb.append(alpha.charAt(Math.abs(random.nextInt()) % alpha.length()));

        return sb.toString();
    }
}
