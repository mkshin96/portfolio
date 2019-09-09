package com.dblab.service;

import com.dblab.controller.IntroductionRestController;
import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.repository.IntroductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.PagedResources.PageMetadata;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class IntroductionService {

    @Autowired
    private IntroductionRepository introductionRepository;

    public void saveIntroduction(IntroductionDto introductionDto, User currentUser) {


        introductionRepository.save(introductionDto.setIntroduction(currentUser));
    }

    public void modifyIntroduction(Long idx, IntroductionDto introductionDto) {
        Introduction modifiedIntroduction = introductionRepository.findByIdx(idx);
        modifiedIntroduction.modifyIntroduction(introductionDto);

        introductionRepository.save(modifiedIntroduction);
    }

    public void deleteIntroduction(Long idx) {
        introductionRepository.deleteById(idx);
    }

    public PagedResources<Introduction> pagedIntroduction(User currentUser, Pageable pageable) {
        Page<Introduction> introductions = introductionRepository.findAllByUser(currentUser, pageable);

        PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(),
                                                    introductions.getNumber(),
                                                    introductions.getTotalElements());

        PagedResources<Introduction> pagedResources = new PagedResources<>(introductions.getContent(),
                                                                            pageMetadata);

        //hateoas 생성
        pagedResources.add(linkTo(methodOn(IntroductionRestController.class).getIntroductions(pageable)).withSelfRel());

        return pagedResources;
    }

    public Resource<Introduction> findIntroductionDetail(Long idx) {
        Introduction introduction = introductionRepository.findByIdx(idx);

        Resource<Introduction> introductionResource = new Resource<>(introduction);
        introductionResource.add(linkTo(methodOn(IntroductionRestController.class).getIntroductionDetail(idx)).withSelfRel());

        return introductionResource;
    }
}
