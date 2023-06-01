package org.neem.neemapp.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.neem.neemapp.jpa.NeemUserRepo;
import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.jpa.PatientRepo;
import org.neem.neemapp.jpa.SubscriptionRepo;
import org.neem.neemapp.model.NeemUser;
import org.neem.neemapp.model.InsurancePlan;
import org.neem.neemapp.model.Patient;
import org.neem.neemapp.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class NeemController {

	@Autowired
	NeemUserRepo userRepo;
	@Autowired
	InsurancePlanRepo planRepo;
	@Autowired
	PatientRepo patientRepo;
	@Autowired
	SubscriptionRepo subscriptionRepo;

	// Example command:
	// curl -X GET http://localhost:8000/rest/user/1
	@GetMapping("/rest/user/{id}")
	EntityModel<NeemUser> getUser(@PathVariable Long id) {

		NeemUser user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(user, linkTo(methodOn(NeemController.class).getUser(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/plan/1
	@GetMapping("/rest/plan/{id}")
	EntityModel<InsurancePlan> getPlan(@PathVariable Long id) {

		InsurancePlan plan = planRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(plan, linkTo(methodOn(NeemController.class).getPlan(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/patient/1
	@GetMapping("/rest/patient/{id}")
	EntityModel<Patient> getPatient(@PathVariable Long id) {

		Patient patient = patientRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		List<Subscription> subscriptions = subscriptionRepo.findByPatientId(id);
		patient.setSubscriptions(subscriptions);

		return EntityModel.of(patient, linkTo(methodOn(NeemController.class).getPatient(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/subscription/1
	@GetMapping("/rest/subscription/{id}")
	EntityModel<Subscription> getSubscription(@PathVariable Long id) {

		Subscription subscription = subscriptionRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(subscription, linkTo(methodOn(NeemController.class).getSubscription(id)).withSelfRel());
	}

}
