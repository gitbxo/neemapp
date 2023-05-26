package org.neem.neemapp.jpa;

import org.neem.neemapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patient, Long> {

}
