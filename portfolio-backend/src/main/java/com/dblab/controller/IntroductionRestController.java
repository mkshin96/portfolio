package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.service.IntroductionService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/introductions")
public class IntroductionRestController {

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private UserService userService;

    private User currentUser;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIntroductions(@PageableDefault Pageable pageable){
        //현재 유저와 매핑
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser = userService.currentUser(user);

        return ResponseEntity.ok(introductionService.pagedIntroduction(currentUser, pageable));
    }

    @GetMapping(value = "/{idx}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIntroductionDetail(@PathVariable("idx") Long idx){
        return ResponseEntity.ok(introductionService.findIntroductionDetail(idx));
    }

    @PostMapping
    public ResponseEntity<?> postIntroductions(@Valid  @RequestBody IntroductionDto introductionDto,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()) return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            introductionService.saveIntroduction(introductionDto, currentUser);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> putIntroductions(@PathVariable("idx") Long idx,
                                              @Valid @RequestBody IntroductionDto introductionDto,
                                              BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }
        else{
            introductionService.modifyIntroduction(idx, introductionDto);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteIntroductions(@PathVariable("idx") Long idx){
        introductionService.deleteIntroduction(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
