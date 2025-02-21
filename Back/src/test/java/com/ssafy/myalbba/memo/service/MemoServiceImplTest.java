//package com.ssafy.myalbba.memo.service;
//
//import com.ssafy.myalbba.Member.entity.Worker;
//import com.ssafy.myalbba.Member.repository.WorkerRepository;
//import com.ssafy.myalbba.memo.dto.MemoRequestDto;
//import com.ssafy.myalbba.memo.entity.Memo;
//import com.ssafy.myalbba.memo.entity.Status;
//import com.ssafy.myalbba.memo.entity.WorkerMemo;
//import com.ssafy.myalbba.memo.repository.MemoRepository;
//import com.ssafy.myalbba.memo.repository.WorkerMemoRepository;
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
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class MemoServiceImplTest {
//
//    @InjectMocks
//    private MemoServiceImpl memoService;
//
//    @Mock
//    private MemoRepository memoRepository;
//
//    @Mock
//    private WorkerRepository workerRepository;
//
//    @Mock
//    private WorkerMemoRepository workerMemoRepository;
//
//    @Test
//    void testRegiterNoice() {
//        // given
//        MemoRequestDto memoRequestDto = new MemoRequestDto("Test Memo", List.of(1L, 2L));
//
//        Worker worker1 = new Worker();
//        worker1.setId(1L);
//        worker1.setName("Worker 1");
//
//        Worker worker2 = new Worker();
//        worker2.setId(2L);
//        worker2.setName("Worker 2");
//
//        Worker worker3 = new Worker();
//        worker3.setId(3L);
//        worker3.setName("Worker 3");
//
//        Memo memo = Memo.builder()
//                .content("Test Memo")
//                .status(Status.NOT_DONE)
//                .build();
//
//        Mockito.when(workerRepository.findById(1L)).thenReturn(Optional.of(worker1));
//        Mockito.when(workerRepository.findById(2L)).thenReturn(Optional.of(worker2));
//        Mockito.when(memoRepository.save(Mockito.any(Memo.class))).thenReturn(memo);
//
//        // when
//        memoService.registerMemo(memoRequestDto, worker3.getName());
//
//        // then
//        Mockito.verify(memoRepository, Mockito.times(1)).save(Mockito.any(Memo.class));
//        Mockito.verify(workerRepository, Mockito.times(2)).findById(Mockito.anyLong());
//    }
//}