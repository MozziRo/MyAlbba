//package com.ssafy.myalbba;
//
//import com.ssafy.myalbba.Member.entity.Worker;
//import com.ssafy.myalbba.Member.repository.WorkerRepository;
//import com.ssafy.myalbba.memo.dto.MemoRequestDto;
//import com.ssafy.myalbba.memo.dto.MemoResponseDto;
//import com.ssafy.myalbba.memo.entity.Memo;
//import com.ssafy.myalbba.memo.entity.Status;
//import com.ssafy.myalbba.memo.entity.WorkerMemo;
//import com.ssafy.myalbba.memo.repository.MemoRepository;
//import com.ssafy.myalbba.memo.repository.WorkerMemoRepository;
//import com.ssafy.myalbba.memo.service.MemoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class MemoIntegrationTest {
//
//    @Autowired
//    private MemoService memoService;
//
//    @Autowired
//    private MemoRepository memoRepository;
//
//    @Autowired
//    private WorkerRepository workerRepository;
//
//    @Autowired
//    private WorkerMemoRepository workerMemoRepository;
//
//    private Worker worker1;
//    private Worker worker2;
//    private Worker worker3;
//
//    @BeforeEach
//    void setUpWorkers() {
//        worker1 = createAndSaveWorker("Worker 1");
//        worker2 = createAndSaveWorker("Worker 2");
//        worker3 = createAndSaveWorker("Worker 3");
//    }
//
//    @Test
//    void testRegisterMemo() {
//        // given
//        MemoRequestDto memoRequestDto = new MemoRequestDto("Test Memo", List.of(worker1.getId(), worker2.getId()));
//
//        // when
//        memoService.registerMemo(memoRequestDto, worker3.getName());
//
//        // then
//        List<WorkerMemo> workerMemos = workerMemoRepository.findAll();
//        assertEquals(2, workerMemos.size());
//
//        WorkerMemo firstMemo = workerMemos.get(0);
//        assertEquals("Test Memo", firstMemo.getMemo().getContent());
//        assertEquals(worker1.getId(), firstMemo.getWorker().getId());
//        assertEquals(worker3.getName(), firstMemo.getMemo().getCreator());
//    }
//
//    @Test
//    void testGetMyMemo() {
//        // given
//        MemoRequestDto memoRequestDto = new MemoRequestDto("Test Memo 1", List.of(worker1.getId()));
//        MemoRequestDto memoRequestDto2 = new MemoRequestDto("Test Memo 2", List.of(worker1.getId()));
//
//        memoService.registerMemo(memoRequestDto, worker3.getName());
//        memoService.registerMemo(memoRequestDto2, worker3.getName());
//
//        // when
//        List<MemoResponseDto> myMemos = memoService.getMyMemo(worker1.getId());
//
//        // then
//        assertEquals(2, myMemos.size());
//
//        MemoResponseDto firstMemo = myMemos.get(0);
//        assertEquals("Test Memo 1", firstMemo.getContent());
//        assertEquals(worker3.getName(), firstMemo.getCreator());
//
//        MemoResponseDto secondMemo = myMemos.get(1);
//        assertEquals("Test Memo 2", secondMemo.getContent());
//        assertEquals(worker3.getName(), secondMemo.getCreator());
//    }
//
//    @Test
//    void testCheckMemo() {
//        // given
//        MemoRequestDto memoRequestDto = new MemoRequestDto("Test Memo", List.of(worker1.getId()));
//        memoService.registerMemo(memoRequestDto, worker3.getName());
//        Long memoId = memoService.getMyMemo(worker1.getId()).get(0).getId();
//
//        // when
//        memoService.checkMemo(memoId);
//
//        // then
//        Memo updatedMemo = memoRepository.findById(memoId)
//                .orElseThrow(() -> new IllegalArgumentException("Memo not found"));
//
//        assertEquals(Status.DONE, updatedMemo.getStatus());
//    }
//
//    private Worker createAndSaveWorker(String name) {
//        Worker worker = new Worker();
//        worker.setName(name);
//        return workerRepository.save(worker);
//    }
//}
