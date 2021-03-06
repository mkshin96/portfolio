package com.dblab.introduction;

import com.dblab.common.TestDescription;
import com.dblab.config.jwtConfig.JwtTokenUtil;
import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
import com.dblab.service.IntroductionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntroductionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IntroductionService introductionService;

    private static final String BEARER = "Bearer ";

    private final String introductionUri = "/api/introductions";

    private final String userUri = "/api/users";

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @TestDescription("자기소개서 객체가 정상적으로 생성되는지 테스트")
    public void 자기소개서_도메인_생성() {
        Introduction introduction = Introduction.builder()
                                                .title1("제목1")
                                                .content1("내용1")
                                                .build();

        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle1()).isEqualTo("제목1");
        assertThat(introduction.getContent1()).isEqualTo("내용1");
    }

    @Test
    @TestDescription("/api/introductions를 get mapping후 페이징 처리되어 잘 동작하는지 확인")
    public void 자기소개서_겟_매핑() throws Exception{
        //유저 생성
        User user = createUserAndRegister(1);
        //jwt 발급
        String jwtToken = BEARER + jwtTokenUtil.generateToken(user);
        //현재유저매핑
        getIntroductions(jwtToken);

        //30개의 자기소개서 생성
        IntStream.rangeClosed(1, 30).forEach(index -> {
            IntroductionDto introductionDto = IntroductionDto.builder()
                    .introductionTitle("자기소개서 제목" + index)
                    .title1("자기소개서 항목1 제목")
                    .content1("자기소개서 항목1 내용")
                    .title2("자기소개서 항목2 제목")
                    .content2("자기소개서 항목2 내용")
                    .title3("자기소개서 항목3 제목")
                    .content3("자기소개서 항목3 내용")
                    .title4("자기소개서 항목4 제목")
                    .content4("자기소개서 항목4 내용")
                    .title5("자기소개서 항목5 제목")
                    .content5("자기소개서 항목5 내용").build();

            introductionService.saveIntroduction(introductionDto, user);
        });

        assertThat(user.getIntroductions().size()).isEqualTo(30);

        //첫번째 페이지 겟매핑
        mockMvc.perform(get(introductionUri)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.projects").exists())
                    .andExpect(jsonPath("_links.main").exists());

        //세번째 페이지 겟매핑
        mockMvc.perform(get(introductionUri)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.projects").exists())
                .andExpect(jsonPath("_links.main").exists());
    }

    @Test
    @TestDescription("자기소개서 생성 동작 확인")
    public void 자기소개서_등록() throws Exception {
        //유저 생성
        User user = createUserAndRegister(4);

        //JWT 발급
        String jwtToken = BEARER + jwtTokenUtil.generateToken(user);

        //유효하지 않은 자기소개서 생성(자기소개서 제목이 null)
        IntroductionDto introductionDto =
                IntroductionDto.builder()
                        .title1("항목1")
                        .build();

        //등록 실패
        MvcResult mvcResult = mockMvc.perform(post(introductionUri)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //유효하지 않은 자기소개서 생성(자기소개서 제목이 "")
        introductionDto.setIntroductionTitle("");

        //등록 실패
        mvcResult = mockMvc.perform(post(introductionUri)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //유효한 자기소개서 생성
        introductionDto.setIntroductionTitle("자기소개서 제목1");

        //현재 유저 매핑
        getIntroductions(jwtToken);

        //등록 성공
        mockMvc.perform(post(introductionUri)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
                    .content(objectMapper.writeValueAsString(introductionDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(print());
    }

    //자기소개서 수정
    @Test
    @TestDescription("자기소개서 수정 동작 확인")
    public void 자기소개서_수정() throws Exception{
        //유저 생성, 등록
        User user = createUserAndRegister(2);

        //jwt 발급
        String jwtToken = BEARER + jwtTokenUtil.generateToken(user);

        //현재유저매핑
        getIntroductions(jwtToken);

        //자기소개서 생성, 등록
        IntroductionDto introductionDto = IntroductionDto.builder()
                                .introductionTitle("자기소개서 제목2")
                                .title1("자기소개서 항목1 제목")
                                .content1("자기소개서 항목1 내용")
                                .title2("자기소개서 항목2 제목")
                                .content2("자기소개서 항목2 내용")
                                .title3("자기소개서 항목3 제목")
                                .content3("자기소개서 항목3 내용")
                                .title4("자기소개서 항목4 제목")
                                .content4("자기소개서 항목4 내용")
                                .title5("자기소개서 항목5 제목")
                                .content5("자기소개서 항목5 내용").build();

        mockMvc.perform(post(introductionUri)
                    .content(objectMapper.writeValueAsString(introductionDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken))
                    .andDo(print())
                    .andExpect(status().isCreated());

        //유효하지않은 데이터로 수정 --> 실패
        introductionDto.setIntroductionTitle("");

        mockMvc.perform(put(introductionUri + "/2")
                        .content(objectMapper.writeValueAsString(introductionDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> { result.getResponse().getContentAsString().equals("필수 항목입니다."); });


        //유효한 값으로 데이터로 수정 --> 성공
        introductionDto.setIntroductionTitle("수정된 자기소개서 제목");

        mockMvc.perform(put(introductionUri + "/2")
                        .content(objectMapper.writeValueAsString(introductionDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

    //자기소개서 삭제
    @Test
    @TestDescription("자기소개서 삭제 동작 확인")
    public void 자기소개서_삭제() throws Exception {
        User user = createUserAndRegister(3);

        //jwt 발급
        String jwtToken = BEARER + jwtTokenUtil.generateToken(user);

        //현재 유저 매핑
        getIntroductions(jwtToken);

        //자기소개서 생성, 등록
        IntroductionDto introductionDto = IntroductionDto.builder()
                .introductionTitle("자기소개서 제목3")
                .title1("자기소개서 항목1 제목")
                .content1("자기소개서 항목1 내용")
                .title2("자기소개서 항목2 제목")
                .content2("자기소개서 항목2 내용")
                .title3("자기소개서 항목3 제목")
                .content3("자기소개서 항목3 내용")
                .title4("자기소개서 항목4 제목")
                .content4("자기소개서 항목4 내용")
                .title5("자기소개서 항목5 제목")
                .content5("자기소개서 항목5 내용").build();

        mockMvc.perform(post(introductionUri)
                .content(objectMapper.writeValueAsString(introductionDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwtToken))
                .andDo(print())
                .andExpect(status().isCreated());

        //자기소개서 삭제 --> 성공
        mockMvc.perform(delete(introductionUri +"/3")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                        .andDo(print())
                        .andExpect(status().isOk());

    }


    @Test
    @TestDescription("잘못된 값이 들어왔을 때 에러 리턴 확인")
    public void 에러_리턴_확인() throws Exception {
        User user = createUserAndRegister(5);

        //jwt 발급
        String jwtToken = BEARER + jwtTokenUtil.generateToken(user);

        //현재 유저 매핑
        getIntroductions(jwtToken);

        //자기소개서 생성, 등록
        IntroductionDto introductionDto = IntroductionDto.builder()
                .introductionTitle("") //잘못된 값
                .title1("자기소개서 항목1 제목")
                .content1("자기소개서 항목1 내용")
                .title2("자기소개서 항목2 제목")
                .content2("자기소개서 항목2 내용")
                .title3("자기소개서 항목3 제목")
                .content3("자기소개서 항목3 내용")
                .title4("자기소개서 항목4 제목")
                .content4("자기소개서 항목4 내용")
                .title5("자기소개서 항목5 제목")
                .content5("자기소개서 항목5 내용").build();

        mockMvc.perform(post("/api/introductions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(introductionDto))
                .header(HttpHeaders.AUTHORIZATION, jwtToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    public User createUserAndRegister(int idx) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testID" + idx);
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");
        userDto.setGitAddr("https://github.com/testId");

        mockMvc.perform(post(userUri)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andDo(print());

        User user = userRepository.findByUsername(userDto.getUsername());
        return user;
    }



    public void getIntroductions(String jwtToken) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(introductionUri)
                        .header("Authorization", jwtToken))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();

        assertThat(mvcResult.getResponse().getContentType()).isEqualTo("application/json;charset=UTF-8");
    }
}
