package com.ssafy.myalbba.notice.service;

import com.ssafy.myalbba.Member.entity.Owner;
import com.ssafy.myalbba.Member.entity.UserEntity;
import com.ssafy.myalbba.Member.repository.OwnerRepository;
import com.ssafy.myalbba.Member.repository.UserRepository;
import com.ssafy.myalbba.common.entity.Store;
import com.ssafy.myalbba.common.repository.StoreRepository;
import com.ssafy.myalbba.notice.dto.NoticeRequestDto;
import com.ssafy.myalbba.notice.dto.NoticeResponseDto;
import com.ssafy.myalbba.notice.entity.Notice;
import com.ssafy.myalbba.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NoticeResponseDto> getNotice(String loginUserEmail) {
        Long ownerId = userRepository.findByEmail(loginUserEmail).getStore().getOwner().getId();

        List<Notice> notices = noticeRepository.findByOwnerId(ownerId);

        return notices.stream()
                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed()) // createdAt으로 내림차순 정렬
                .map(NoticeResponseDto::from) // NoticeResponseDto로 변환
                .toList();
    }

    @Override
    public NoticeResponseDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(RuntimeException::new);
        return NoticeResponseDto.from(notice);
    }

    @Override
    public NoticeResponseDto registerNotice(String email, NoticeRequestDto noticeRequestDto) {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);

        Notice notice = Notice.createNotice(noticeRequestDto.getTitle(), noticeRequestDto.getContent());

        owner.addNotice(notice);
        return NoticeResponseDto.from(noticeRepository.save(notice));
    }

    @Override
    public void updateNotice(NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(noticeRequestDto.getId())
                .orElseThrow(RuntimeException::new);

        notice.updateNotice(noticeRequestDto.getTitle(), noticeRequestDto.getContent());
        noticeRepository.save(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

}
