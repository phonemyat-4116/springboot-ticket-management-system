package com.phone_myat.ticketapp.repositories;

import com.phone_myat.ticketapp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO users (id, username, email, created_at, updated_at)
        VALUES (:id, :username, :email, NOW(), NOW())
        ON CONFLICT (id) DO NOTHING 
        """, nativeQuery = true)
    void upsertUser(@Param("id") UUID keycloakUserId,
                    @Param("username") String username,
                    @Param("email") String email);
}

/*
Request A                Database

INSERT

                     row inserted

Request B

INSERT

                     conflict on id
                     DO NOTHING
 */