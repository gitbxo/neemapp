package org.neem.neemapp.jpa;

import org.neem.neemapp.model.NeemUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeemUserRepo extends JpaRepository<NeemUser, Long> {

}
