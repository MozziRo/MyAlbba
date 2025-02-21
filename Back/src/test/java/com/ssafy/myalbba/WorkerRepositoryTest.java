//package com.ssafy.myalbba;
//
//import com.ssafy.myalbba.Member.entity.Worker;
//import com.ssafy.myalbba.Member.repository.WorkerRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//public class WorkerRepositoryTest {
//    @Autowired
//    WorkerRepository workerRepository;
//
//    @BeforeEach
//    public void clear() {
//        workerRepository.deleteAll();
//
//        Worker worker1 = new Worker();
//        worker1.setOwnerId(1);
//        worker1.setStoreId(1);
//        worker1.setName("KimAlba");
//        worker1.setEmail("KimAlba@naver.com");
//        worker1.setPassword("1234");
//        worker1.setPhone_number("01012345678");
//        worker1.setHourly_salary(10030.0);
//        worker1.setBank("국민은행");
//        worker1.setAccount("548615465186548");
//
//        workerRepository.save(worker1);
//
//        Worker worker2 = new Worker();
//        worker2.setOwnerId(1);
//        worker2.setStoreId(1);
//        worker2.setName("LeeAlba");
//        worker2.setEmail("LeeAlba@naver.com");
//        worker2.setPassword("1234");
//        worker2.setPhone_number("01012345678");
//        worker2.setHourly_salary(10030.0);
//        worker2.setBank("국민은행");
//        worker2.setAccount("548615465186548");
//
//        workerRepository.save(worker2);
//    }
//
//    @Test
//    public void saveTest() {
//        Worker worker = new Worker();
//        worker.setOwnerId(1);
//        worker.setStoreId(1);
//        worker.setName("ChoiAlba");
//        worker.setEmail("ChoiAlba@naver.com");
//        worker.setPassword("1234");
//        worker.setPhone_number("01012345678");
//        worker.setHourly_salary(10030.0);
//        worker.setBank("국민은행");
//        worker.setAccount("548615465186548");
//
//        workerRepository.save(worker);
//
//        List<Worker> workers = workerRepository.findAll();
//        Assertions.assertEquals(workers.size(), 3);
//    }
//
//    @Test
//    public void findByOwnerIdTest() {
//        List<Worker> workers = workerRepository.findByOwnerId(1);
//        Assertions.assertEquals(workers.size(), 2);
//    }
//
//    @Test
//    public void findByStoreIdTest() {
//        List<Worker> workers = workerRepository.findByStoreId(1);
//        Assertions.assertEquals(workers.size(), 2);
//    }
//
//    @Test
//    public void findByEmailTest() {
//        Optional<Worker> optionalWorker = workerRepository.findByEmail("KimAlba@naver.com");
//        Worker worker = optionalWorker.get();
//        Assertions.assertEquals(worker.getEmail(), "KimAlba@naver.com");
//    }
//
//    @Test
//    public void findByEmailAndPasswordTest() {
//        String email = "KimAlba@naver.com";
//        String password = "1234";
//
//        Optional<Worker> optionalWorker = workerRepository.findByEmailAndPassword(email, password);
//        Worker worker = optionalWorker.get();
//        Assertions.assertEquals(worker.getEmail(), "KimAlba@naver.com");
//    }
//
//    @Test
//    public void findByNameTest() {
//        Optional<Worker> optionalWorker = workerRepository.findByName("LeeAlba");
//        Worker worker = optionalWorker.get();
//        Assertions.assertEquals(worker.getName(), "LeeAlba");
//    }
//
//    @Test
//    public void updateTest() {
//        Optional<Worker> optionalWorker = workerRepository.findByName("KimAlba");
//        Worker worker = optionalWorker.get();
//
//        worker.setName("Park Alba");
//        workerRepository.save(worker);
//
//        optionalWorker = workerRepository.findByName("Park Alba");
//        worker = optionalWorker.get();
//        Assertions.assertEquals(worker.getEmail(), "KimAlba@naver.com");
//    }
//
//}
