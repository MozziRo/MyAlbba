//package com.ssafy.myalbba.notice.service;
//
//import com.ssafy.myalbba.Member.entity.Owner;
//import com.ssafy.myalbba.Member.repository.OwnerRepository;
//import com.ssafy.myalbba.notice.dto.NoticeRequestDto;
//import com.ssafy.myalbba.notice.dto.NoticeResponseDto;
//import com.ssafy.myalbba.notice.entity.Notice;
//import com.ssafy.myalbba.notice.repository.NoticeRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class NoticeServiceImplTest {
//
//    @Mock
//    private NoticeRepository noticeRepository;
//
//    @Mock
//    private OwnerRepository ownerRepository;
//
//    @InjectMocks
//    private NoticeServiceImpl noticeService;
//
//    @Test
//    void testGetNotice(){
//        // given
//        List< Notice> notices = List.of(
//                Notice.createNotice("Title 1", "Notice 1"),
//                Notice.createNotice("Title 2", "Notice 2")
//        );
//
//        Mockito.when(noticeRepository.findAll()).thenReturn(notices);
//
//        // when
//        List<NoticeResponseDto> result = noticeService.getNotice();
//
//        // then
//        assertThat(result.size()).isEqualTo(2);
//        assertThat(result.get(0).getContent()).isEqualTo("Notice 1");
//        assertThat(result.get(1).getContent()).isEqualTo("Notice 2");
//    }
//
//    @Test
//    void testRegisterNotice() {
//        //given
//        String name = "권신범";
//        String email = "test@example.com";
//        String phone = "010-1234-5678";
//        String title = "New Title";
//        String content = "New Notice";
//
//        Owner owner = Owner.builder()
//                .name(name)
//                .email(email)
//                .phoneNumber(phone)
//                .build();
//
//        Mockito.when(ownerRepository.findByEmail(email)).thenReturn(Optional.of(owner));
//
//        // when
//        noticeService.registerNotice(email, new NoticeRequestDto(title, content));
//
//        // then
//        Mockito.verify(noticeRepository, Mockito.times(1)).save(Mockito.any(Notice.class));
//        assertThat(owner.getNotices().size()).isEqualTo(1);
//        assertThat(owner.getNotices().get(0).getContent()).isEqualTo(content);
//    }
//}