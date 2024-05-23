package io.codef.subject.infra.persistence;

import io.codef.subject.domain.ConnectedId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectedIdRepository extends JpaRepository<ConnectedId, Long> {

    Optional<ConnectedId> findByOrganizationCode(String organizationCode);
}
