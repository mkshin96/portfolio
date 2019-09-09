package com.dblab.introduction;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.dto.UserDto;
import com.dblab.repository.IntroductionRepository;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.IntroductionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntroductionRestTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDetails userDetails;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private IntroductionRepository introductionRepository;

    private Introduction introduction;

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        //유저 생성
        UserDto userDto = new UserDto();
        userDto.setUsername("testId");
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");

        //유저 등록
        mockMvc.perform(post("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()))
                .andExpect(status().isCreated());

        //로그인
        mockMvc.perform(formLogin().user("testId").password("testPassword"))
                .andExpect(forwardedUrl("/login/success"));

        userDetails = customUserDetailsService.loadUserByUsername("testId");

        assertThat(userDetails.getUsername()).isEqualTo("testId");
    }

    @Test
    public void 겟_매핑_테스트() throws Exception {
        User user = userRepository.findByIdx(1L);

        //30개의 자기소개서 생성
        for(int i = 0; i < 30; i++){
            IntroductionDto introductionDto = new IntroductionDto();
            introductionDto.setTitle("제목" + i);
            introductionDto.setGrowth("성장과정" + i);
            introductionService.saveIntroduction(introductionDto, user);
        }

        assertThat(user.getIntroductions().size()).isEqualTo(30);

        mockMvc.perform(get("/api/introductions")
                .with(csrf())
                .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk());

        //다른 유저 생성
        UserDto userDto = new UserDto();
        userDto.setUsername("testId2");
        userDto.setPassword("testPassword");
        userDto.setEmail("test2@gmail.com");

        //유저 등록
        mockMvc.perform(post("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()))
                .andExpect(status().isCreated());

        //데이터베이스 확인
        User user2 = userRepository.findByIdx(2L);
        assertThat(user2).isNotNull();
        assertThat(user2.getUsername()).isEqualTo("testId2");

        //30개의 자기소개서 생성
        for (int i = 30; i < 60; i++){
            IntroductionDto introductionDto = new IntroductionDto();
            introductionDto.setTitle("제목" + i);
            introductionDto.setGrowth("성장과정" + i);
            introductionService.saveIntroduction(introductionDto, user2);
        }

        //자기소개서 등록 확인
        assertThat(user2.getIntroductions().size()).isEqualTo(30);

        userDetails = customUserDetailsService.loadUserByUsername("testId2");

        mockMvc.perform(get("/api/introductions")
                        .with(csrf())
                        .with(user(userDetails)))
                        .andDo(print())
                        .andExpect(status().isOk());

    }

    @Test
    public void 자기소개서_등록_테스트() throws Exception {

        mockMvc.perform(get("/api/introductions")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        //자기소개서 생성
        IntroductionDto introductionDto = new IntroductionDto();
        introductionDto.setTitle("제목");
        introductionDto.setGrowth("성장과정");

        //자기소개서 등록
        mockMvc.perform(post("/api/introductions")
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .with(user(userDetails)));

        //데이터 베이스 확인
        Introduction introduction = introductionRepository.findByIdx(1L);

        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("제목");
        assertThat(introduction.getUser().getUsername()).isEqualTo("testId");
    }

    @Test
    public void 자기소개서_수정_테스트() throws Exception {

        //현재 유저와 매핑
        mockMvc.perform(get("/api/introductions")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        //자기소개서 생성
        IntroductionDto introductionDto = new IntroductionDto();
        introductionDto.setTitle("제목");
        introductionDto.setGrowth("성장과정");

        //자기소개서 등록
        mockMvc.perform(post("/api/introductions")
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .with(user(userDetails)));

        //자기소개서 수정
        introductionDto.setTitle("수정된 제목");
        introductionDto.setGrowth("수정된 성장과정");

        //자기소개서 등록
        mockMvc.perform(put("/api/introductions/1")
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        //데이터베이스 확인
        introduction = introductionRepository.findByIdx(1L);

        assertThat(introduction.getTitle()).isEqualTo("수정된 제목");
        assertThat(introduction.getGrowth()).isEqualTo("수정된 성장과정");
    }

    @Test
    public void 자기소개서_삭제_테스트() throws Exception {

        //현재 유저와 매핑
        mockMvc.perform(get("/api/introductions")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        //자기소개서 생성
        IntroductionDto introductionDto = new IntroductionDto();
        introductionDto.setTitle("제목");
        introductionDto.setGrowth("성장과정");

        //자기소개서 등록
        mockMvc.perform(post("/api/introductions")
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        //자기소개서 삭제
        mockMvc.perform(delete("/api/introductions/1")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        //데이터베이스 확인
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNull();
    }

    @Test
    public void 자기소개서_세부사항_테스트() throws Exception {
        User currentUser = userRepository.findByIdx(1L);

        //자기소개서 등록

        IntStream.rangeClosed(1, 30).forEach(
                index -> introductionService.saveIntroduction(
                        IntroductionDto.builder()
                                .title("제목" + index)
                                .growth("성장 과정" + index)
                                .aspiration("입사 후 포부").build(), currentUser)

        );

        //자기소개서 등록 확인
        Introduction introduction = introductionRepository.findByIdx(1L);

        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("제목1");
        assertThat(introduction.getGrowth()).isEqualTo("성장 과정1");

        //매핑 후 반환 값 확인
        mockMvc.perform(get("/api/introductions/1").with(csrf()).with(user(userDetails)))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/introductions/2").with(csrf()).with(user(userDetails)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
