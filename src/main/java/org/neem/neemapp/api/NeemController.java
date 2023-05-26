package org.neem.neemapp.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.neem.neemapp.jpa.NeemUserRepo;
import org.neem.neemapp.model.NeemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class NeemController {

  @Autowired NeemUserRepo userRepo;

  // Example command:
  // curl -X GET http://localhost:8000/user/1
  @GetMapping("/user/{id}")
  EntityModel<NeemUser> getuser(@PathVariable Long id) {

    NeemUser user = userRepo.findById(id)
      .orElseThrow(() -> new UserNotFoundException(id));

    return EntityModel.of(user,
        linkTo(methodOn(NeemController.class).getuser(id)).withSelfRel());
  }

}
