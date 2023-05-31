package org.neem.neemapp.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
	// curl -X GET http://localhost:8000/user/1
	@GetMapping("/user/{id}")
	EntityModel<NeemUser> getUser(@PathVariable Long id) {

		NeemUser user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(user, linkTo(methodOn(NeemController.class).getUser(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/plan/1
	@GetMapping("/plan/{id}")
	EntityModel<InsurancePlan> getPlan(@PathVariable Long id) {

		InsurancePlan plan = planRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(plan, linkTo(methodOn(NeemController.class).getPlan(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/patient/1
	@GetMapping("/patient/{id}")
	EntityModel<Patient> getPatient(@PathVariable Long id) {

		Patient patient = patientRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(patient, linkTo(methodOn(NeemController.class).getPatient(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/subscription/1
	@GetMapping("/subscription/{id}")
	EntityModel<Subscription> getSubscription(@PathVariable Long id) {

		Subscription subscription = subscriptionRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		return EntityModel.of(subscription, linkTo(methodOn(NeemController.class).getSubscription(id)).withSelfRel());
	}

}
