package org.neem.neemapp.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.neem.neemapp.jpa.NeemUserRepo;
import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.jpa.PatientRepo;
import org.neem.neemapp.jpa.SubscriptionRepo;
import org.neem.neemapp.model.NeemUser;
import org.neem.neemapp.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		NeemUser user = userRepo.findById(id).orElseThrow(() -> new NeemAppException.UserNotFoundException(id));

		return EntityModel.of(user, linkTo(methodOn(NeemController.class).getUser(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/plan/1
	@GetMapping("/rest/plan/{id}")
	EntityModel<InsurancePlan> getPlan(@PathVariable String id) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, id);
		if (plan == null) {
			throw new NeemAppException.PlanNotFoundException(id);
		}

		return EntityModel.of(plan, linkTo(methodOn(NeemController.class).getPlan(id)).withSelfRel());
	}

	// Example command:
	// curl -X POST http://localhost:8000/rest/plan
	@PostMapping("/rest/plan")
	InsurancePlan createPlan(@RequestBody InsurancePlan plan) {
		return InsurancePlan.createPlan(planRepo, plan);
	}

	// Example command:
	// curl -X PUT http://localhost:8000/rest/plan/1
	@PutMapping("/rest/plan/{id}")
	InsurancePlan updatePlan(@PathVariable String id, @RequestBody InsurancePlan plan) {
		InsurancePlan updated = InsurancePlan.updatePlan(planRepo, id, plan);
		if (updated == null) {
			throw new NeemAppException.PlanNotFoundException(id);
		}

		return updated;
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/patient/1
	@GetMapping("/rest/patient/{id}")
	EntityModel<Patient> getPatient(@PathVariable String id) {
		Patient patient = patientRepo.findById(UUID.fromString(id))
				.orElseThrow(() -> new NeemAppException.PatientNotFoundException(id));
		patient.setSubscriptions(subscriptionRepo.findByPatientId(patient.getId()));

		return EntityModel.of(patient, linkTo(methodOn(NeemController.class).getPatient(id)).withSelfRel());
	}

	// Example command:
	// curl -X GET http://localhost:8000/rest/subscription/1/2
	@GetMapping("/rest/subscription/{patient_id}/{plan_id}")
	EntityModel<Subscription> getSubscription(@PathVariable String patient_id, @PathVariable String plan_id) {
		Subscription subscription = Subscription.findByPatientIdAndPlanId(subscriptionRepo, planRepo, patient_id,
				plan_id);
		if (subscription == null) {
			throw new NeemAppException.SubscriptionNotFoundException(patient_id, plan_id);
		}

		return EntityModel.of(subscription,
				linkTo(methodOn(NeemController.class).getSubscription(patient_id, plan_id)).withSelfRel());
	}

	// Example command:
	// curl -X POST http://localhost:8000/rest/subscription/1/2
	@PostMapping("/rest/subscription/{patient_id}/{plan_id}")
	Subscription createSubscription(@PathVariable String patient_id, @PathVariable String plan_id,
			@RequestBody Subscription subscription) {
		return Subscription.createSubscription(subscriptionRepo, planRepo, patient_id, plan_id, subscription);
	}

	// Example command:
	// curl -X PUT http://localhost:8000/rest/subscription/1/2
	@PutMapping("/rest/subscription/{patient_id}/{plan_id}")
	Subscription updateSubscription(@PathVariable String patient_id, @PathVariable String plan_id,
			@RequestBody Subscription subscription) {
		Subscription updated = Subscription.updateSubscription(subscriptionRepo, planRepo, patient_id, plan_id,
				subscription);
		if (updated == null) {
			throw new NeemAppException.SubscriptionNotFoundException(patient_id, plan_id);
		}

		return updated;
	}

}
